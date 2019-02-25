package moe.pine.emotions.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@ConfigurationProperties(prefix = "gravatar")
public class GravatarProperties {
    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private List<String> images;

    @NotNull
    private List<String> addresses;
}
