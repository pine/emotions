package moe.pine.emotions.bookmeter;

import com.google.common.annotations.VisibleForTesting;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class WebClient {
    private static final Duration TIMEOUT = Duration.ofSeconds(60L);

    @VisibleForTesting
    static final String BASE_URL = "https://bookmeter.com/";

    @VisibleForTesting
    static final String USER_AGENT =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36";

    static final String LOGIN_PATH = "/login";

    private final org.springframework.web.reactive.function.client.WebClient webClient;

    public WebClient(
        final org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder
    ) {
        webClient = webClientBuilder.baseUrl(BASE_URL).build();
    }

    @Value
    @Builder
    public static class GetLoginResponse {
        private String body;
        private MultiValueMap<String, String> cookies;
    }

    public GetLoginResponse getLogin() {
        final ClientResponse clientResponse =
            webClient.get()
                .uri(LOGIN_PATH)
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .exchange()
                .block(TIMEOUT);
        Objects.requireNonNull(clientResponse);

        if (clientResponse.statusCode() != HttpStatus.OK) {
            throw new RuntimeException(
                String.format("Illegal status code :: statusCode=%s", clientResponse.statusCode()));
        }

        final String body = clientResponse.bodyToMono(String.class).block(TIMEOUT);
        if (StringUtils.isEmpty(body)) {
            throw new RuntimeException("An empty body received");
        }

        return GetLoginResponse.builder()
            .body(body)
            .cookies(CookieUtils.normalize(clientResponse.cookies()))
            .build();
    }

    @Value
    @Builder
    public static class PostLoginResponse {
        private MultiValueMap<String, String> cookies;
    }

    public PostLoginResponse postLogin(
        final MultiValueMap<String, String> formData,
        final MultiValueMap<String, String> cookies
    ) {
        final ClientResponse clientResponse =
            webClient.post()
                .uri(LOGIN_PATH)
                .header(HttpHeaders.USER_AGENT, USER_AGENT)
                .syncBody(formData)
                .cookies(builder -> builder.addAll(cookies))
                .exchange()
                .block(TIMEOUT);
        Objects.requireNonNull(clientResponse);

        final HttpStatus statusCode = clientResponse.statusCode();
        if (!statusCode.is3xxRedirection()) {
            throw new RuntimeException(
                String.format("Illegal status code received. :: statusCode=%s", statusCode));
        }

        final String location =
            clientResponse.headers().asHttpHeaders().getFirst(HttpHeaders.LOCATION);
        Objects.requireNonNull(location);

        if (location.contains(LOGIN_PATH)) {
            throw new RuntimeException("Incorrect email address or password");
        }

        return PostLoginResponse.builder()
            .cookies(CookieUtils.normalize(clientResponse.cookies()))
            .build();
    }
}
