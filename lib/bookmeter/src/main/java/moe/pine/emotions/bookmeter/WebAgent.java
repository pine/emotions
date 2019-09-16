package moe.pine.emotions.bookmeter;

import com.google.common.annotations.VisibleForTesting;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
class WebAgent {
    private static final Duration TIMEOUT = Duration.ofSeconds(60L);

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

    WebAgent(
        final WebClient.Builder webClientBuilder
    ) {
        this(webClientBuilder, BASE_URL);
    }

    @VisibleForTesting
    WebAgent(
        final WebClient.Builder webClientBuilder,
        final String baseUrl
    ) {
        webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    /**
     * GET /login
     */
    GetLoginResponse getLogin() {
        final ClientResponse clientResponse = get(LOGIN_PATH, null);
        final String body = clientResponse.bodyToMono(String.class).block(TIMEOUT);
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
    ) {
        final ClientResponse clientResponse = post(LOGIN_PATH, formData, cookies);
        final HttpStatus statusCode = clientResponse.statusCode();
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
    public GetAccountResponse getAccount(
        final MultiValueMap<String, String> cookies
    ) {
        final ClientResponse clientResponse = get(ACCOUNT_PATH, cookies);
        final String body = clientResponse.bodyToMono(String.class).block(TIMEOUT);
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
    public static class GetAccountResponse {
        private String body;
        private MultiValueMap<String, String> cookies;
    }

    /**
     * POST /account
     */
    public void postAccount(
        final MultiValueMap<String, HttpEntity<?>> formData,
        final MultiValueMap<String, String> cookies
    ) {
        final ClientResponse clientResponse = post(ACCOUNT_PATH, formData, cookies);
        final HttpStatus statusCode = clientResponse.statusCode();
        if (!statusCode.is3xxRedirection()) {
            throw new RuntimeException(
                String.format("Illegal status code received. :: statusCode=%s", statusCode));
        }
    }

    /**
     * GET
     */
    private ClientResponse get(
        final String path,
        @Nullable final MultiValueMap<String, String> cookies
    ) {
        final ClientResponse clientResponse =
            webClient.get()
                .uri(path)
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .cookies(builder -> {
                    if (MapUtils.isNotEmpty(cookies)) {
                        builder.addAll(cookies);
                    }
                })
                .exchange()
                .block(TIMEOUT);
        if (clientResponse == null) {
            throw new RuntimeException("An empty response received.");
        }
        if (clientResponse.statusCode() != HttpStatus.OK) {
            throw new RuntimeException(
                String.format("Illegal status code :: statusCode=%d", clientResponse.rawStatusCode()));
        }

        return clientResponse;
    }

    /**
     * POST
     */
    private ClientResponse post(
        final String path,
        final MultiValueMap<String, ?> formData,
        final MultiValueMap<String, String> cookies
    ) {
        final ClientResponse clientResponse =
            webClient.post()
                .uri(path)
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .syncBody(formData)
                .cookies(builder -> builder.addAll(cookies))
                .exchange()
                .block(TIMEOUT);
        if (clientResponse == null) {
            throw new RuntimeException("An empty response received.");
        }

        return clientResponse;
    }
}
