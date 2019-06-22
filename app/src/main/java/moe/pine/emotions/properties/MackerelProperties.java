package moe.pine.emotions.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
@ConfigurationProperties("mackerel")
public class MackerelProperties {
    private @NotBlank String apiKey;
    private @NotNull Graphs graphs;

    @Data
    @Validated
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Graphs {
        private @NotNull Graph elapsedTime;
    }

    @Data
    @Validated
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Graph {
        private @NotBlank String name;
    }
}
