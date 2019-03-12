package moe.pine.emotions.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "cloudstorage")
public class CloudStorageProperties {
    private String credentials;
    private List<Image> images;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        private String bucket;
        private String name;
    }
}
