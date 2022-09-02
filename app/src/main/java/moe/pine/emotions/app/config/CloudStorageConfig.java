package moe.pine.emotions.app.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.app.properties.CloudStorageProperties;
import moe.pine.emotions.cloudstorage.CloudStorage;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableConfigurationProperties(CloudStorageProperties.class)
@Slf4j
public class CloudStorageConfig {
    @Bean
    public CloudStorage cloudStorage(
        CloudStorageProperties cloudStorageProperties
    ) throws IOException {
        final String credentials = cloudStorageProperties.getCredentials();

        try (InputStream stream = IOUtils.toInputStream(credentials, StandardCharsets.UTF_8)) {
            return CloudStorage.fromStream(stream);
        }
    }
}
