package moe.pine.emotions.properties;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Value
@NonFinal
@Validated
@ConstructorBinding
@ConfigurationProperties("twitter")
public class TwitterProperties {
    @NotBlank String consumerKey;
    @NotBlank String consumerSecret;
    @NotBlank String accessToken;
    @NotBlank String accessTokenSecret;
    @NotNull Duration connectionTimeout;
    @NotNull Duration readTimeout;
}
