package moe.pine.emotions.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.cloudstorage.CloudStorage;
import moe.pine.emotions.properties.CloudStorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.UncheckedIOException;

@Configuration
@EnableConfigurationProperties(CloudStorageProperties.class)
@Slf4j
public class CloudStorageConfig {
    @Bean
    public CloudStorage cloudStorage(
        @Nonnull final CloudStorageProperties cloudStorageProperties,
        @Nonnull final ResourceLoader resourceLoader
    ) {
        final String location = cloudStorageProperties.getCredentials();
        log.info("Loading GCP credentials file '{}'", location);

        final Resource resource = resourceLoader.getResource(location);
        try {
            return CloudStorage.fromStream(resource.getInputStream());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
