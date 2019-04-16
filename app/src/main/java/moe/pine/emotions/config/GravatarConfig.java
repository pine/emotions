package moe.pine.emotions.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.Gravatar;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.properties.GravatarProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nonnull;
import java.util.Random;

@Configuration
@EnableConfigurationProperties(GravatarProperties.class)
@Slf4j
public class GravatarConfig {
    @Bean
    public GravatarClient gravatarClient(
        @Nonnull final GravatarProperties gravatarProperties
    ) {
        return new GravatarClient(gravatarProperties.getEmail());
    }

    @Bean
    public Gravatar gravatar(
        @Nonnull final Random random,
        @Nonnull final GravatarProperties gravatarProperties,
        @Nonnull final GravatarClient gravatarClient
    ) {
        return new Gravatar(random, gravatarClient, gravatarProperties.getPassword());
    }

}
