package moe.pine.emotions.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mackerel")
public class MackerelProperties {
    private String apiKey;
    private Graphs graphs;

    @Data
    public static class Graphs {
        private Graph elapsedTime;
    }

    @Data
    public static class Graph {
        private String name;
    }
}
