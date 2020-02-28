package moe.pine.emotions.properties;

import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@NonFinal
@Validated
@ConstructorBinding
@ConfigurationProperties("slack")
public class SlackProperties {
    @NotNull List<Workspace> workspaces;

    @Value
    @NonFinal
    @Validated
    @ConstructorBinding
    public static class Workspace {
        @NotBlank String id;
        @NotBlank String token;
    }
}
