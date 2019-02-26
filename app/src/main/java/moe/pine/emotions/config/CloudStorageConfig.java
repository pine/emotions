package moe.pine.emotions.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.cloudstorage.CloudStorage;
import moe.pine.emotions.properties.CloudStorageProperties;
import org.jetbrains.annotations.NotNull;
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
        @NotNull final ResourceLoader resourceLoader
    ) throws IOException {
        final Resource resource = resourceLoader.getResource("");
        return new CloudStorage(resource.getInputStream());
    }
}
