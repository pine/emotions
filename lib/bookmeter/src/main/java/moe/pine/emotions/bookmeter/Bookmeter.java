package moe.pine.emotions.bookmeter;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Slf4j
public class Bookmeter {
    private final Fetcher fetcher;
    private final Parser parser;
    private final FormDataBuilder formDataBuilder;

    public Bookmeter(
        final String email,
        final String password,
        final WebClient.Builder webClientBuilder
    ) {
        this(
            new Fetcher(webClientBuilder),
            new Parser(),
            new FormDataBuilder(email, password));
    }

    @VisibleForTesting
    Bookmeter(
        final Fetcher fetcher,
        final Parser parser,
        final FormDataBuilder formDataBuilder
    ) {
        this.fetcher = Objects.requireNonNull(fetcher);
        this.parser = Objects.requireNonNull(parser);
        this.formDataBuilder = Objects.requireNonNull(formDataBuilder);
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
                formDataBuilder.buildLoginFormData(authenticityToken),
                getLoginResponse.getCookies());
        log.debug("Login successful");

        final Fetcher.GetAccountResponse getAccountResponse =
            fetcher.getAccount(postLoginResponse.getCookies());
        final Parser.AccountFormData accountFormData =
            parser.parseAccountForm(getAccountResponse.getBody());
        log.debug("Found account form :: formData={}", accountFormData);

        fetcher.postAccount(
            formDataBuilder.buildAccountFormData(
                accountFormData.getAuthenticityToken(),
                accountFormData.getName(),
                image),
            getAccountResponse.getCookies());
        log.debug("Update profile image successful");
    }
}
