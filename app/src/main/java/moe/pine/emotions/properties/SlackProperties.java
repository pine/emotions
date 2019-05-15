package moe.pine.emotions.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "slack")
public class SlackProperties {
    private List<Workspace> workspaces;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Workspace {
        private String id;
        private String token;
    }
}
