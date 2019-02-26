package moe.pine.emotions.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "slack")
public class SlackProperties {
    private List<Channel> channels;

    @Data
    @SuppressWarnings("WeakerAccess")
    public static class Channel {
        private String workspace;
        private String token;
    }
}
