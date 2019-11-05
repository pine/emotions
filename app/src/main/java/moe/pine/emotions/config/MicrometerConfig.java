package moe.pine.emotions.config;

import moe.pine.emotions.properties.MicrometerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MicrometerProperties.class)
public class MicrometerConfig {
}
