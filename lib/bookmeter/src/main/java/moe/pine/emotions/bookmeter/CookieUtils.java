package moe.pine.emotions.bookmeter;

import lombok.experimental.UtilityClass;
import org.springframework.data.util.StreamUtils;
import org.springframework.http.ResponseCookie;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@UtilityClass
public class CookieUtils {
    public MultiValueMap<String, String> normalize(
        final MultiValueMap<String, ResponseCookie> cookies
    ) {
        return cookies
            .entrySet()
            .stream()
            .flatMap(v1 -> v1.getValue().stream().map(v2 -> Map.entry(v1.getKey(), v2.getValue())))
            .collect(StreamUtils.toMultiMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
