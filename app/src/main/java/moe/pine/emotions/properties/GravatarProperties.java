package moe.pine.emotions.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Data
@ConfigurationProperties("gravatar")
public class GravatarProperties {
    private String email;
    private String password;
    private List<String> images;
    private List<String> addresses;
}
