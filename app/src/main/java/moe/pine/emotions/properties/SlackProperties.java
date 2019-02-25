package moe.pine.emotions.properties;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "slack")
public class SlackProperties {
    @NotNull
    public List<Channel> channels;

    @Data
    public static class Channel {
        @NotNull
        public String workspace;

        @NotNull
        public String token;
    }
}
