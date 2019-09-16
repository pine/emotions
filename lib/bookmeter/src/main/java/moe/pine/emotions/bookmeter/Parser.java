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

        return parseAuthenticityToken(form);
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

        return AccountFormData.builder()
            .authenticityToken(parseAuthenticityToken(form))
            .name(parseName(form))
            .build();
    }

    private String parseAuthenticityToken(final Element element) {
        final Element authenticityTokenElement = element.selectFirst("[name=\"authenticity_token\"]");
        if (authenticityTokenElement == null) {
            throw new RuntimeException("Authenticity token element not found.");
        }

        final String authenticityToken = authenticityTokenElement.val();
        if (StringUtils.isEmpty(authenticityToken)) {
            throw new RuntimeException("`authenticity_token` is not found.");
        }

        return authenticityToken;
    }

    private String parseName(final Element element) {
        final Element nameElement = element.selectFirst("[name=\"name\"]");
        if (nameElement == null) {
            throw new RuntimeException("Name element not found.");
        }

        final String name = nameElement.val();
        if (StringUtils.isEmpty(name)) {
            throw new RuntimeException("`name` is not found.");
        }

        return name;
    }
}
