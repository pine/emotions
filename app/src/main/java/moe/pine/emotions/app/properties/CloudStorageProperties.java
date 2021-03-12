package moe.pine.emotions.app.properties;

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
@ConfigurationProperties("cloudstorage")
public class CloudStorageProperties {
    @NotBlank String credentials;
    @NotNull List<Image> images;

    @Value
    @NonFinal
    @Validated
    @ConstructorBinding
    public static class Image {
        @NotBlank String bucket;
        @NotBlank String name;
    }
}
