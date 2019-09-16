package moe.pine.emotions.bookmeter;

import org.junit.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.Assert.assertEquals;

public class CookieUtilsTest {
    @Test
    public void normalizeTest() {
        final MultiValueMap<String, ResponseCookie> cookies = new LinkedMultiValueMap<>();
        cookies.add("foo", ResponseCookie.from("foo", "1").build());
        cookies.add("foo", ResponseCookie.from("foo", "2").build());
        cookies.add("bar", ResponseCookie.from("bar", "3").build());

        final MultiValueMap<String, String> expected = new LinkedMultiValueMap<>();
        expected.add("foo", "1");
        expected.add("foo", "2");
        expected.add("bar", "3");

        final MultiValueMap<String, String> actual = CookieUtils.normalize(cookies);

        assertEquals(expected, actual);
    }
}
