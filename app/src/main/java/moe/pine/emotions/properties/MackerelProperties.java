package moe.pine.emotions.properties;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@NonFinal
@Validated
@ConstructorBinding
@ConfigurationProperties("mackerel")
public class MackerelProperties {
    @NotBlank String apiKey;
    @NotNull Graphs graphs;

    @Value
    @NonFinal
    @Validated
    @ConstructorBinding
    public static class Graphs {
        @NotNull Graph elapsedTime;
    }

    @Value
    @NonFinal
    @Validated
    @ConstructorBinding
    public static class Graph {
        @NotBlank String name;
    }
}
