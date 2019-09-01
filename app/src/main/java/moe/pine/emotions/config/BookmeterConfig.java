package moe.pine.emotions.config;

import moe.pine.emotions.bookmeter.Bookmeter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BookmeterConfig {
    @Bean
    public Bookmeter bookmeter(
        final WebClient.Builder webClientBuilder
    ) {
        return new Bookmeter("pinemz@gmail.com", "", webClientBuilder);
    }
}
