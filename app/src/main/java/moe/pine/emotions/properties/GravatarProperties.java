package moe.pine.emotions.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@Validated
@ConfigurationProperties("gravatar")
public class GravatarProperties {
    private @NotBlank String email;
    private @NotBlank String password;
    private @NotNull List<String> images;
    private @NotNull List<String> addresses;
}
