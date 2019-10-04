package moe.pine.emotions.bookmeter;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.springutils.NamedByteArrayResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
public class Bookmeter {
    private final String email;
    private final String password;
    private final Fetcher fetcher;
    private final Parser parser;
    private final FormDataBuilder formDataBuilder;

    public Bookmeter(
        final String email,
        final String password,
        final WebClient.Builder webClientBuilder
    ) {
        this(
            email,
            password,
            new Fetcher(webClientBuilder),
            new Parser(),
            new FormDataBuilder(email, password));
    }

    @VisibleForTesting
    Bookmeter(
        final String email,
        final String password,
        final Fetcher fetcher,
        final Parser parser,
        final FormDataBuilder formDataBuilder
    ) {
        this.email = email;
        this.password = password;
        this.fetcher = fetcher;
        this.parser = parser;
        this.formDataBuilder = formDataBuilder;
    }

    public void updateProfileImage(final byte[] image) throws InterruptedException {
        final Fetcher.GetLoginResponse getLoginResponse = fetcher.getLogin();
        final String authenticityToken = parser.parseLoginForm(getLoginResponse.getBody());
        if (StringUtils.isEmpty(authenticityToken)) {
            throw new RuntimeException("`authenticity_token` is not found.");
        }
        log.debug("Found authenticity token :: authenticityToken={}", authenticityToken);

        final Fetcher.PostLoginResponse postLoginResponse =
            fetcher.postLogin(
                buildLoginFormData(authenticityToken),
                getLoginResponse.getCookies());
        log.debug("Login successful");

        final Fetcher.GetAccountResponse getAccountResponse =
            fetcher.getAccount(postLoginResponse.getCookies());
        final Parser.AccountFormData accountFormData =
            parser.parseAccountForm(getAccountResponse.getBody());
        log.debug("Found account form :: formData={}", accountFormData);

        fetcher.postAccount(
            buildAccountFormData(accountFormData, image),
            getAccountResponse.getCookies());
        log.debug("Update profile image successful");
    }

    @VisibleForTesting
    MultiValueMap<String, String> buildLoginFormData(final String authenticityToken) {
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("utf8", "✓");
        formData.add("authenticity_token", authenticityToken);
        formData.add("session[email_address]", email);
        formData.add("session[password]", password);
        formData.add("session[keep]", "0");
        formData.add("session[keep]", "1");

        return formData;
    }

    @VisibleForTesting
    MultiValueMap<String, HttpEntity<?>> buildAccountFormData(
        final Parser.AccountFormData accountFormData,
        final byte[] image
    ) {
        final MultipartBodyBuilder builder = new MultipartBodyBuilder();
        builder.part("utf8", "✓");
        builder.part("_method", "put");
        builder.part("authenticity_token", accountFormData.getAuthenticityToken());
        builder.part("name", accountFormData.getName());

        final Resource resource = new NamedByteArrayResource(image, "image.png");
        builder.part("icon", resource, MediaType.IMAGE_PNG);

        return builder.build();
    }
}
