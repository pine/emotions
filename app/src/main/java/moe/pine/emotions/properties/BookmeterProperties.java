package moe.pine.emotions.properties;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Value
@NonFinal
@Validated
@ConstructorBinding
@ConfigurationProperties("bookmeter")
public class BookmeterProperties {
    @NotBlank String email;
    @NotBlank String password;
}
