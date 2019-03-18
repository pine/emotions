package moe.pine.emotions.gravatar.xmlrpc.models;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class UserImageFactory {
    /**
     * @see <a href="https://en.gravatar.com/site/implement/xmlrpc/">XML-RPC API</a>
     */
    @Nonnull
    public List<UserImage> from(@Nonnull final Object data) {
        if (!(data instanceof Map<?, ?>)) {
            throw new IllegalArgumentException(
                String.format("Unexpected data format :: %s", String.valueOf(data)));
        }

        @SuppressWarnings("unchecked") final Map<Object, Object> entries = (Map<Object, Object>) data;

        return entries
            .entrySet()
            .stream()
            .map(entry -> {
                if (!(entry.getKey() instanceof String)) {
                    throw new IllegalArgumentException(
                        String.format("Unexpected data format :: %s", String.valueOf(entry.getKey())));
                }
                if (!(entry.getValue() instanceof Object[])) {
                    throw new IllegalArgumentException(
                        String.format("Unexpected data format :: %s", String.valueOf(entry.getValue())));
                }

                final Object[] values = (Object[]) entry.getValue();
                if (values.length != 2) {
                    throw new IllegalArgumentException(String.format(
                        "Unexpected array length :: expected=2, actual=%d", values.length));
                }
                if (!(values[1] instanceof String)) {
                    throw new IllegalArgumentException(
                        String.format("Unexpected data format :: %s", String.valueOf(values[1])));
                }

                return UserImage.builder()
                    .hash((String) entry.getKey())
                    .url((String) values[1])
                    .build();
            })
            .collect(Collectors.toList());
    }
}
