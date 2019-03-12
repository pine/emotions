package moe.pine.emotions.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@Slf4j
public class AppConfig {
    @Bean
    public Random random() {
        return new Random();
    }
}
