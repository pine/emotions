package moe.pine.emotions.app.config;

import moe.pine.emotions.mackerel.Mackerel;
import moe.pine.emotions.app.properties.MackerelProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MackerelProperties.class)
public class MackerelConfig {
    @Bean
    public Mackerel mackerel(
        RestTemplateBuilder restTemplateBuilder,
        MackerelProperties mackerelProperties
    ) {
        return new Mackerel(
            restTemplateBuilder,
            mackerelProperties.getApiKey()
        );
    }
}
