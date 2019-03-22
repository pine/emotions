package moe.pine.emotions.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Random;

@Configuration
@Slf4j
public class AppConfig {
    @Bean
    public Random random() {
        return new Random();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        final var executor = new ThreadPoolTaskExecutor();
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        return executor;
    }
}

