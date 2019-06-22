package moe.pine.emotions.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Validated
@ConfigurationProperties("slack")
public class SlackProperties {
    private @NotNull List<Workspace> workspaces;

    @Data
    @Validated
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Workspace {
        private @NotBlank String id;
        private @NotBlank String token;
    }
}
