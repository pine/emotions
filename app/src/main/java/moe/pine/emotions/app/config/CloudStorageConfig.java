package moe.pine.emotions.app.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.cloudstorage.CloudStorage;
import moe.pine.emotions.app.properties.CloudStorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
@EnableConfigurationProperties(CloudStorageProperties.class)
@Slf4j
public class CloudStorageConfig {
    @Bean
    public CloudStorage cloudStorage(
        CloudStorageProperties cloudStorageProperties,
        ResourceLoader resourceLoader
    ) throws IOException {
        String location = cloudStorageProperties.getCredentials();
        log.info("Loading GCP credentials file '{}'", location);

        Resource resource = resourceLoader.getResource(location);
        return CloudStorage.fromStream(resource.getInputStream());
    }
}
