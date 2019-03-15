package moe.pine.emotions.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.properties.TwitterProperties;
import moe.pine.emotions.twitter.Twitter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;

@Configuration
@EnableConfigurationProperties(TwitterProperties.class)
@Slf4j
public class TwitterConfig {
    @Bean
    public Twitter twitter(@Nonnull final TwitterProperties twitterProperties) {
        return new Twitter(
            twitterProperties.getConsumerKey(),
            twitterProperties.getConsumerSecret(),
            twitterProperties.getAccessToken(),
            twitterProperties.getAccessTokenSecret()
        );
    }
}
