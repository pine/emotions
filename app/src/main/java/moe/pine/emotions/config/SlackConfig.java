package moe.pine.emotions.config;

import moe.pine.emotions.properties.SlackProperties;
import moe.pine.emotions.slack.Slack;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(SlackProperties.class)
public class SlackConfig {
    @Bean
    public Slack slack(
        final WebClient.Builder webClientBuilder
    ) {
        return new Slack(webClientBuilder);
    }
}
