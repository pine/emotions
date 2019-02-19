package moe.pine.emotions.gravatar.xmlrpc.models;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.Map;

@UtilityClass
@Slf4j
public class UserImageFactory {
    /**
     * @see <a href="https://en.gravatar.com/site/implement/xmlrpc/">XML-RPC API</a>
     */
    @Nonnull
    public UserImage[] fromArray(@Nonnull final Object data) {
        if (!(data instanceof Map<?, ?>)) {
            throw new RuntimeException(
                String.format("Unexpected data format :: %s", data.toString()));
        }

        @SuppressWarnings("unchecked") final Map<Object, Object> entries = (Map<Object, Object>) data;

        return entries
            .entrySet()
            .stream()
            .map(entry -> {
                if (!(entry.getKey() instanceof String)) {
                    throw new RuntimeException(
                        String.format("Unexpected data format :: %s", entry.getKey()));
                }
                if (!(entry.getValue() instanceof Object[])) {
                    throw new RuntimeException(
                        String.format("Unexpected data format :: %s", entry.getValue()));
                }

                final Object[] values = (Object[]) entry.getValue();
                if (values.length != 2) {
                    throw new RuntimeException(
                        String.format("Unexpected array length :: expected=2, actual=%d", values.length));
                }
                if (!(values[1] instanceof String)) {
                    throw new RuntimeException(
                        String.format("Unexpected data format :: %s", values[1]));
                }

                return UserImage.builder()
                    .hash((String) entry.getKey())
                    .url((String) values[1])
                    .build();
            })
            .toArray(UserImage[]::new);
    }
}
