package moe.pine.emotions.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.Gravatar;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.properties.GravatarProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@EnableConfigurationProperties(GravatarProperties.class)
@Slf4j
public class GravatarConfig {
    @Bean
    public GravatarClient gravatarClient(
        final GravatarProperties gravatarProperties
    ) {
        return new GravatarClient(gravatarProperties.getEmail());
    }

    @Bean
    public Gravatar gravatar(
        final GravatarProperties gravatarProperties,
        final GravatarClient gravatarClient,
        final Random random
    ) {
        return new Gravatar(gravatarClient, gravatarProperties.getPassword(), random);
    }

}
