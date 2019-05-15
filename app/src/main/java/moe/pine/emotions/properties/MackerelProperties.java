package moe.pine.emotions.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mackerel")
public class MackerelProperties {
    private String apiKey;
    private Graphs graphs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Graphs {
        private Graph elapsedTime;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Graph {
        private String name;
    }
}
