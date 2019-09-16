package moe.pine.emotions.bookmeter;

import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

class Parser {
    String parseLoginForm(final String body) {
        final Document document = Jsoup.parse(body);
        final Element form = document.selectFirst("#js_sessions_new_form form");
        if (form == null) {
            throw new RuntimeException("Login form element not found.");
        }

        final Element authenticityTokenElement = form.selectFirst("[name=\"authenticity_token\"]");
        if (authenticityTokenElement == null) {
            throw new RuntimeException("Authenticity token element not found.");
        }

        final String authenticityToken = authenticityTokenElement.val();
        if (StringUtils.isEmpty(authenticityToken)) {
            throw new RuntimeException("`authenticity_token` is not found.");
        }

        return authenticityToken;
    }

    @Value
    @Builder
    static class AccountFormData {
        private String authenticityToken;
        private String name;
    }

    AccountFormData parseAccountForm(final String body) {
        final Document document = Jsoup.parse(body);
        final Element form = document.selectFirst("#js_account_form");
        if (form == null) {
            throw new RuntimeException("Account form element not found.");
        }

        final Element authenticityTokenElement = form.selectFirst("[name=\"authenticity_token\"]");
        final Element name = form.selectFirst("[name=\"name\"]");

        return AccountFormData.builder()
            .authenticityToken(authenticityTokenElement.val())
            .name(name.val())
            .build();
    }
}
