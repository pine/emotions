package moe.pine.emotions.gravatar.xmlrpc.models;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class StatusFactory {
    /**
     * @see <a href="https://en.gravatar.com/site/implement/xmlrpc/">XML-RPC API</a>
     */
    
    public Map<String, Boolean> from( final Object data) {
        if (!(data instanceof Map<?, ?>)) {
            throw new IllegalArgumentException(
                String.format("Unexpected data format :: %s", data.toString()));
        }

        @SuppressWarnings("unchecked") final Map<Object, Object> entries = (Map<Object, Object>) data;

        return entries
            .entrySet()
            .stream()
            .map(entry -> {
                if (!(entry.getKey() instanceof String)) {
                    throw new IllegalArgumentException(
                        String.format("Unexpected data format :: %s", entry.getKey()));
                }
                if (!(entry.getValue() instanceof Boolean)) {
                    throw new IllegalArgumentException(
                        String.format("Unexpected data format :: %s", entry.getValue()));
                }

                final Boolean status = (Boolean) entry.getValue();
                return Pair.of((String) entry.getKey(), status);
            })
            .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    }
}
