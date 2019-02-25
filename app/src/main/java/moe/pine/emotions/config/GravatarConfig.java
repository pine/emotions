package moe.pine.emotions.config;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.Gravatar;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.properties.GravatarProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class GravatarConfig {
    @NonNull
    private final GravatarProperties gravatarProperties;

    @Bean
    public GravatarClient gravatarClient() {
        return new GravatarClient(gravatarProperties.getEmail());
    }

    @Bean
    public  Gravatar gravatar(final GravatarClient gravatarClient) {
        return new Gravatar(gravatarClient, gravatarProperties.getPassword());
    }

}
