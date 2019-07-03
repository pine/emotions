package moe.pine.emotions.config;

import moe.pine.emotions.properties.SlackProperties;
import moe.pine.emotions.slack.Slack;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SlackProperties.class)
public class SlackConfig {
    @Bean
    public Slack slack(
        final RestTemplateBuilder restTemplateBuilder
    ) {
        return new Slack(restTemplateBuilder);
    }
}
