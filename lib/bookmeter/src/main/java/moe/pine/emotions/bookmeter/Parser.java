package moe.pine.emotions.bookmeter;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Parser {
    public void aaa(final String body) {
        final Document document = Jsoup.parse(body);
        final Element form = document.selectFirst("#js_sessions_new_form form");
        final Element authenticityTokenElement = form.selectFirst("[name=\"authenticity_token\"]");
        if (authenticityTokenElement == null) {
            throw new RuntimeException();
        }

        final String authenticityToken = authenticityTokenElement.attr("value");
        if (StringUtils.isEmpty(authenticityToken)) {
            throw new RuntimeException("`authenticity_token` is not found.");
        }
    }
}
