package moe.pine.emotions.bookmeter;

import com.google.common.annotations.VisibleForTesting;
import io.netty.handler.codec.http.cookie.Cookie;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import moe.pine.reactor.interruptible.MonoUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.Cookies;
import reactor.netty.http.client.HttpClientResponse;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
class Fetcher {
    @VisibleForTesting
    static final Duration TIMEOUT = Duration.ofSeconds(60L);

    @VisibleForTesting
    static final String BASE_URL = "https://bookmeter.com/";

    @VisibleForTesting
    static final String USER_AGENT =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36";

    @VisibleForTesting
    static final String LOGIN_PATH = "/login";

    @VisibleForTesting
    static final String ACCOUNT_PATH = "/account";

    private final WebClient webClient;

    Fetcher(
        final WebClient.Builder webClientBuilder
    ) {
        webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    /**
     * GET /login
     */
    GetLoginResponse getLogin() throws InterruptedException {
        final ClientResponse clientResponse = get(LOGIN_PATH, null);
        final String body = MonoUtils.block(clientResponse.bodyToMono(String.class), TIMEOUT);
        if (StringUtils.isEmpty(body)) {
            throw new RuntimeException("An empty body received.");
        }

        return GetLoginResponse.builder()
            .body(body)
            .cookies(CookieUtils.normalize(clientResponse.cookies()))
            .build();
    }

    @Value
    @Builder
    static class GetLoginResponse {
        private String body;
        private MultiValueMap<String, String> cookies;
    }

    /**
     * POST /login
     */
    PostLoginResponse postLogin(
        final MultiValueMap<String, String> formData,
        final MultiValueMap<String, String> cookies
    ) throws InterruptedException {
        final ClientResponse clientResponse = post(LOGIN_PATH, formData, cookies);
        final HttpStatus statusCode = clientResponse.statusCode();
        MonoUtils.block(clientResponse.bodyToMono(String.class), TIMEOUT);

        if (!statusCode.is3xxRedirection()) {
            throw new RuntimeException(
                String.format("Illegal status code received. :: statusCode=%s", statusCode.value()));
        }

        final String location =
            clientResponse.headers().asHttpHeaders().getFirst(HttpHeaders.LOCATION);
        if (StringUtils.isEmpty(location)) {
            throw new RuntimeException("An empty `Location` header received.");
        }
        if (location.contains(LOGIN_PATH)) {
            throw new RuntimeException("Incorrect email address or password");
        }

        return PostLoginResponse.builder()
            .cookies(CookieUtils.normalize(clientResponse.cookies()))
            .build();
    }

    @Value
    @Builder
    static class PostLoginResponse {
        private MultiValueMap<String, String> cookies;
    }

    /**
     * GET /account
     */
    GetAccountResponse getAccount(
        final MultiValueMap<String, String> cookies
    ) throws InterruptedException {
        final ClientResponse clientResponse = get(ACCOUNT_PATH, cookies);
        final String body = MonoUtils.block(clientResponse.bodyToMono(String.class), TIMEOUT);
        if (StringUtils.isEmpty(body)) {
            throw new RuntimeException("An empty body received");
        }

        return GetAccountResponse.builder()
            .body(body)
            .cookies(CookieUtils.normalize(clientResponse.cookies()))
            .build();
    }

    @Value
    @Builder
    static class GetAccountResponse {
        private String body;
        private MultiValueMap<String, String> cookies;
    }

    /**
     * POST /account
     */
    void postAccount(
        final MultiValueMap<String, HttpEntity<?>> formData,
        final MultiValueMap<String, String> cookies
    ) throws InterruptedException {
        final ClientResponse clientResponse = post(ACCOUNT_PATH, formData, cookies);
        final HttpStatus statusCode = clientResponse.statusCode();
        MonoUtils.block(clientResponse.bodyToMono(String.class), TIMEOUT);

        if (!statusCode.is3xxRedirection()) {
            throw new RuntimeException(
                String.format("Illegal status code received. :: statusCode=%s",
                    clientResponse.rawStatusCode()));
        }
    }

    /**
     * GET
     */
    @VisibleForTesting
    ClientResponse get(
        final String path,
        @Nullable final MultiValueMap<String, String> cookies
    ) throws InterruptedException {
        final ClientResponse clientResponse = MonoUtils.block(
            webClient.get()
                .uri(path)
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .cookies(builder -> {
                    if (MapUtils.isNotEmpty(cookies)) {
                        builder.addAll(cookies);
                    }
                })
                .exchange(), TIMEOUT);
        Objects.requireNonNull(clientResponse);

        if (clientResponse.statusCode() != HttpStatus.OK) {
            throw new RuntimeException(
                String.format("Illegal status code :: statusCode=%d", clientResponse.rawStatusCode()));
        }

        patchBrokenCookies(clientResponse);
        return clientResponse;
    }

    /**
     * POST
     */
    @VisibleForTesting
    ClientResponse post(
        final String path,
        final MultiValueMap<String, ?> formData,
        final MultiValueMap<String, String> cookies
    ) throws InterruptedException {
        final ClientResponse clientResponse = MonoUtils.block(
            webClient.post()
                .uri(path)
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .bodyValue(formData)
                .cookies(builder -> builder.addAll(cookies))
                .exchange(), TIMEOUT);

        patchBrokenCookies(clientResponse);
        return Objects.requireNonNull(clientResponse);
    }

    /**
     * Monkey patch to a broken bookmeter's Set-Cookie header
     */
    private void patchBrokenCookies(final ClientResponse clientResponse) {
        try {
            clientResponse.cookies();
        } catch (IllegalArgumentException e) {
            final ClientHttpResponse clientHttpResponse =
                Whitebox.getInternalState(clientResponse, "response");
            final HttpClientResponse httpClientResponse =
                Whitebox.getInternalState(clientHttpResponse, "response");
            final Object responseState = Whitebox.getInternalState(httpClientResponse, "responseState");
            final Cookies cookieHolder = Whitebox.getInternalState(responseState, "cookieHolder");
            final Map<CharSequence, Set<Cookie>> cachedCookies = cookieHolder.getCachedCookies();

            cachedCookies.forEach((key, value) -> {
                value.forEach(cookie -> {
                    // XXX: set-cookie: _session_id_elk=xxx; domain=.bookmeter.com; path=/; Max-Age=1; HttpOnly
                    if (StringUtils.startsWith(cookie.domain(), ".")) {
                        cookie.setDomain(cookie.domain().substring(1));
                    }
                });
            });
        }
    }
}
