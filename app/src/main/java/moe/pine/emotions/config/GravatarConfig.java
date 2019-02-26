package moe.pine.emotions.config;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.Gravatar;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.properties.GravatarProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(GravatarProperties.class)
@Slf4j
public class GravatarConfig {
    @Bean
    public GravatarClient gravatarClient(
        @NotNull final GravatarProperties gravatarProperties
    ) {
        return new GravatarClient(gravatarProperties.getEmail());
    }

    @Bean
    public Gravatar gravatar(
        @NotNull final GravatarProperties gravatarProperties,
        @NotNull final GravatarClient gravatarClient
    ) {
        return new Gravatar(gravatarClient, gravatarProperties.getPassword());
    }

}
