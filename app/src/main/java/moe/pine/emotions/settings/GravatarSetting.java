package moe.pine.emotions.settings;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;

@Data
@ConfigurationProperties(prefix = "gravatar")
public class GravatarSetting {
    private @NotNull String email;
}
