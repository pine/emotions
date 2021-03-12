package moe.pine.emotions.app.config;

import moe.pine.emotions.bookmeter.Bookmeter;
import moe.pine.emotions.app.properties.BookmeterProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(BookmeterProperties.class)
public class BookmeterConfig {
    @Bean
    public Bookmeter bookmeter(
        WebClient.Builder webClientBuilder,
        BookmeterProperties bookmeterProperties
    ) {
        return new Bookmeter(
            bookmeterProperties.getEmail(),
            bookmeterProperties.getPassword(),
            webClientBuilder);
    }
}
