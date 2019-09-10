package moe.pine.emotions.bookmeter;

import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Parser {
    public String parseLoginForm(final String body) {
        final Document document = Jsoup.parse(body);
        final Element form = document.selectFirst("#js_sessions_new_form form");
        final Element authenticityTokenElement = form.selectFirst("[name=\"authenticity_token\"]");
        if (authenticityTokenElement == null) {
            throw new RuntimeException();
        }

        final String authenticityToken = authenticityTokenElement.val();
        if (StringUtils.isEmpty(authenticityToken)) {
            throw new RuntimeException("`authenticity_token` is not found.");
        }

        return authenticityToken;
    }

    @Value
    @Builder
    public static class AccountFormData {
        private String authenticityToken;
        private String name;
    }

    public AccountFormData parseAccountForm(final String body) {
        final Document document = Jsoup.parse(body);
        final Element form = document.selectFirst("#js_account_form");
        final Element authenticityTokenElement = form.selectFirst("[name=\"authenticity_token\"]");
        final Element name = form.selectFirst("[name=\"name\"]");

        return AccountFormData.builder()
            .authenticityToken(authenticityTokenElement.val())
            .name(name.val())
            .build();
    }
}
