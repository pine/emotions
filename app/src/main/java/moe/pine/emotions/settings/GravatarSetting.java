package moe.pine.emotions.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "gravatar")
public class GravatarSetting {
    private final String email;
}
