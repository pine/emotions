package moe.pine.emotions.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ConfigurationProperties("micrometer")
public class MicrometerProperties {
    private @NotNull Metrics metrics;

    @Data
    @Validated
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metrics {
        private @NotNull Metric elapsedTime;
    }

    @Data
    @Validated
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metric {
        private @NotBlank String name;
    }
}
