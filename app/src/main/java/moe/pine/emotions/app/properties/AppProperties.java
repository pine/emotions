package moe.pine.emotions.app.properties;

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
@ConfigurationProperties("app")
public class AppProperties {
    @NotBlank String siteUrl;
}
