package moe.pine.emotions.bookmeter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;
import java.util.Map;

@Slf4j
public class Bookmeter {
    private static final Duration TIMEOUT = Duration.ofSeconds(60L);

    private final String email;
    private final String password;
    private final WebClient webClient;
    private final Parser parser;

    public Bookmeter(
        final String email,
        final String password,
        final org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder
    ) {
        this.email = email;
        this.password = password;
        this.webClient = new WebClient(webClientBuilder);
        this.parser = new Parser();
    }

    @SneakyThrows
    public void updateProfileImage(final byte[] image) {
        final WebClient.GetLoginResponse getLoginResponse = webClient.getLogin();
        final Document document = Jsoup.parse(getLoginResponse.getBody());
        final Element form = document.selectFirst("#js_sessions_new_form form");
        final Element authenticityTokenElement = form.selectFirst("[name=\"authenticity_token\"]");
        if (authenticityTokenElement == null) {
            throw new RuntimeException();
        }

        final String authenticityToken = authenticityTokenElement.attr("value");
        if (StringUtils.isEmpty(authenticityToken)) {
            throw new RuntimeException("`authenticity_token` is not found.");
        }
        log.debug("Found authenticity token :: authenticityToken={}", authenticityToken);

        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("utf8", "✓");
        formData.add("authenticity_token", authenticityToken);
        formData.add("session[email_address]", email);
        formData.add("session[password]", password);
        formData.add("session[keep]", "0");
        formData.add("session[keep]", "1");

        final WebClient.PostLoginResponse postLoginResponse =
            webClient.postLogin(formData, getLoginResponse.getCookies());
        log.debug("Login successful");


    }
}
