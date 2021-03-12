package moe.pine.emotions.app.properties;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Value
@NonFinal
@Validated
@ConstructorBinding
@ConfigurationProperties("gravatar")
public class GravatarProperties {
    @NotBlank String email;
    @NotBlank String password;
    @NotNull List<String> images;
    @NotNull List<String> addresses;
}
