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
@ConfigurationProperties("cloudstorage")
public class CloudStorageProperties {
    private @NotBlank String credentials;
    private @NotNull List<Image> images;

    @Data
    @Validated
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        private @NotBlank String bucket;
        private @NotBlank String name;
    }
}
