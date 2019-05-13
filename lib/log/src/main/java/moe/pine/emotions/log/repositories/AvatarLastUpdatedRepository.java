package moe.pine.emotions.log.repositories;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import moe.pine.emotions.log.models.AvatarType;
import moe.pine.emotions.log.utils.AvatarLastUpdatedKeyBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AvatarLastUpdatedRepository {
    private final StringRedisTemplate redisTemplate;
    private final AvatarLastUpdatedKeyBuilder keyBuilder;
    private final ZoneId zoneId;

    public void set(
        @Nonnull final AvatarType avatarType,
        @Nonnull final LocalDateTime lastUpdatedAt
    ) {
        final String key = keyBuilder.buildKey(avatarType);
        final long value = lastUpdatedAt.atZone(zoneId).toEpochSecond();
        redisTemplate.opsForValue().set(key, String.valueOf(value));
    }

    @Nonnull
    public List<Pair<AvatarType, LocalDateTime>> mget(
        @Nonnull final List<AvatarType> avatarTypes
    ) {
        final List<String> keys = avatarTypes.stream()
            .map(keyBuilder::buildKey)
            .collect(Collectors.toUnmodifiableList());
        final List<String> values = redisTemplate.opsForValue().multiGet(keys);

        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }

        final var items = ImmutableList.<Pair<AvatarType, LocalDateTime>>builder();
        for (int i = 0; i < values.size(); ++i) {
            if (values.get(i) != null) {
                final AvatarType avatarType = avatarTypes.get(i);
                final long epochSeconds = Long.parseLong(values.get(i));
                final Instant instant = Instant.ofEpochSecond(epochSeconds);
                final LocalDateTime dateTime = LocalDateTime.ofInstant(instant, zoneId);
                items.add(Pair.of(avatarType, dateTime));
            }
        }

        return items.build();
    }
}
