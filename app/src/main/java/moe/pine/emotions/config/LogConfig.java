package moe.pine.emotions.config;

import moe.pine.emotions.log.repositories.AvatarUpdatedRepository;
import moe.pine.emotions.log.utils.AvatarUpdatedKeyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;
import java.time.ZoneId;

@Configuration
public class LogConfig {
    @Bean
    public AvatarUpdatedKeyBuilder avatarUpdatedKeyBuilder() {
        return new AvatarUpdatedKeyBuilder();
    }

    @Bean
    public AvatarUpdatedRepository avatarUpdatedRepository(
        @Nonnull final StringRedisTemplate redisTemplate,
        @Nonnull final AvatarUpdatedKeyBuilder keyBuilder,
        @Nonnull final ZoneId zoneId
    ) {
        return new AvatarUpdatedRepository(
            redisTemplate,
            keyBuilder,
            zoneId
        );
    }
}
