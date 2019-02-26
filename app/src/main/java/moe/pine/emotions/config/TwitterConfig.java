package moe.pine.emotions.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.properties.TwitterProperties;
import moe.pine.emotions.twitter.Twitter;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TwitterProperties.class)
@Slf4j
public class TwitterConfig {
    @Bean
    public Twitter twitter(@NotNull final TwitterProperties twitterProperties) {
        return new Twitter(
            twitterProperties.getConsumerKey(),
            twitterProperties.getConsumerSecret(),
            twitterProperties.getAccessToken(),
            twitterProperties.getAccessTokenSecret()
        );
    }
}
