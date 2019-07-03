package moe.pine.emotions.config;

import moe.pine.emotions.log.AvatarUpdatedRepository;
import moe.pine.emotions.log.AvatarUpdatedKeyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.ZoneId;

@Configuration
public class LogConfig {
    @Bean
    public AvatarUpdatedKeyBuilder avatarUpdatedKeyBuilder() {
        return new AvatarUpdatedKeyBuilder();
    }

    @Bean
    public AvatarUpdatedRepository avatarUpdatedRepository(
        final StringRedisTemplate redisTemplate,
        final AvatarUpdatedKeyBuilder keyBuilder,
        final ZoneId zoneId
    ) {
        return new AvatarUpdatedRepository(
            redisTemplate,
            keyBuilder,
            zoneId
        );
    }
}
