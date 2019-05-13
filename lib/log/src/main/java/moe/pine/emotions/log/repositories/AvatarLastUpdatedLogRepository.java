package moe.pine.emotions.log.repositories;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.log.models.AvatarType;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RequiredArgsConstructor
public class AvatarLastUpdatedLogRepository {
    private final StringRedisTemplate redisTemplate;

    public void update(
        @Nonnull final AvatarType avatarType,
        @Nonnull final LocalDateTime lastUpdatedAt
    ) {
        final String key = "avatar_last_updated_at:" + avatarType.getId(); // TODO
        final long value = lastUpdatedAt.atZone(ZoneId.systemDefault()).toEpochSecond();
        redisTemplate.opsForValue().set(key, String.valueOf(value));
    }

    public LocalDateTime find(
        @Nonnull final AvatarType avatarType
    ) {
        return LocalDateTime.now(); // TODO
    }
}
