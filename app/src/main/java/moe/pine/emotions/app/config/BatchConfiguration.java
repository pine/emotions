package moe.pine.emotions.app.config;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.app.batch.BookmeterTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BatchConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job bookmeterJob(Step bookmeterStep) {
        return jobBuilderFactory.get("bookmeter")
            .flow(bookmeterStep)
            .end()
            .build();
    }

    @Bean
    public Step bookmeterStep(BookmeterTasklet bookmeterTasklet) {
        return stepBuilderFactory.get("main")
            .tasklet(bookmeterTasklet)
            .build();
    }
}
