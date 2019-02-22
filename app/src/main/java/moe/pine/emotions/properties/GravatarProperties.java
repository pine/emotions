package moe.pine.emotions.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;


@Data
@Validated
@ConfigurationProperties(prefix = "gravatar")
public class GravatarProperties {
    private @NotNull String email;
    private @NotNull String password;
}
