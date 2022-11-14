package moe.pine.emotions.app.config;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.app.batch.BookmeterTasklet;
import moe.pine.emotions.app.batch.GravatarTasklet;
import moe.pine.emotions.app.batch.TwitterTasklet;
import org.springframework.batch.core.Job;
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
    public Job bookmeterJob(BookmeterTasklet bookmeterTasklet) {
        var step = stepBuilderFactory.get("main")
            .tasklet(bookmeterTasklet)
            .build();

        return jobBuilderFactory.get("bookmeter")
            .flow(step)
            .end()
            .build();
    }

    @Bean
    public Job gravatarJob(GravatarTasklet gravatarTasklet) {
        var step = stepBuilderFactory.get("main")
            .tasklet(gravatarTasklet)
            .build();

        return jobBuilderFactory.get("gravatar")
            .flow(step)
            .end()
            .build();
    }

    @Bean
    public Job twitterJob(TwitterTasklet twitterTasklet) {
        var step = stepBuilderFactory.get("main")
            .tasklet(twitterTasklet)
            .build();

        return jobBuilderFactory.get("twitter")
            .flow(step)
            .end()
            .build();
    }
}
