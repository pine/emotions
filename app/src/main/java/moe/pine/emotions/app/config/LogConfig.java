package moe.pine.emotions.app.config;

import moe.pine.emotions.log.AvatarUpdatedRepository;
import moe.pine.emotions.log.AvatarUpdatedKeyBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
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
        ReactiveStringRedisTemplate redisTemplate,
        AvatarUpdatedKeyBuilder keyBuilder,
        ZoneId zoneId
    ) {
        return new AvatarUpdatedRepository(
            redisTemplate,
            keyBuilder,
            zoneId
        );
    }
}
