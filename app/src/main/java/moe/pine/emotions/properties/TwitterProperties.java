package moe.pine.emotions.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Validated
@ConfigurationProperties("twitter")
public class TwitterProperties {
    private @NotBlank String consumerKey;
    private @NotBlank String consumerSecret;
    private @NotBlank String accessToken;
    private @NotBlank String accessTokenSecret;
    private @NotNull Duration connectionTimeout;
    private @NotNull Duration readTimeout;
}
