package moe.pine.emotions.log.repositories;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import moe.pine.emotions.log.models.AvatarType;
import moe.pine.emotions.log.utils.AvatarUpdatedKeyBuilder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Nonnull;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

@Repository
@RequiredArgsConstructor
public class AvatarUpdatedRepository {
    private final StringRedisTemplate redisTemplate;
    private final AvatarUpdatedKeyBuilder keyBuilder;
    private final ZoneId zoneId;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void set(
        @Nonnull final AvatarType avatarType,
        @Nonnull final LocalDateTime updatedAt
    ) {
        checkNotNull(avatarType);
        checkNotNull(updatedAt);

        final String key = keyBuilder.buildKey(avatarType);
        final long value = updatedAt.atZone(zoneId).toEpochSecond();
        redisTemplate.opsForValue().set(key, String.valueOf(value));
    }

    @Nonnull
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public List<Pair<AvatarType, LocalDateTime>> mget(
        @Nonnull final List<AvatarType> avatarTypes
    ) {
        checkNotNull(avatarTypes);

        final List<String> keys = avatarTypes.stream()
            .map(Objects::requireNonNull)
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
                final LocalDateTime updatedAt = LocalDateTime.ofInstant(instant, zoneId);
                items.add(Pair.of(avatarType, updatedAt));
            }
        }

        return items.build();
    }
}
