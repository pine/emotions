package moe.pine.emotions.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = "slack")
public class SlackProperties {
    @NotNull
    public List<Channel> channels;

    @Data
    @Validated
    public static class Channel {
        @NotNull
        public String workspace;

        @NotNull
        public String token;
    }
}
