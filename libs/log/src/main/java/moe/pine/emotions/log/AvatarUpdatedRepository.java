package moe.pine.emotions.log;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AvatarUpdatedRepository {
    private final ReactiveStringRedisTemplate redisTemplate;
    private final AvatarUpdatedKeyBuilder keyBuilder;
    private final ZoneId zoneId;

    public void set(
        final AvatarType avatarType,
        final LocalDateTime updatedAt
    ) {
        Objects.requireNonNull(avatarType);
        Objects.requireNonNull(updatedAt);

        final String key = keyBuilder.buildKey(avatarType);
        final long value = updatedAt.atZone(zoneId).toEpochSecond();
        redisTemplate.opsForValue().set(key, String.valueOf(value)).block();
    }

    public Optional<LocalDateTime> get(final AvatarType avatarType) {
        Objects.requireNonNull(avatarType);

        final String key = keyBuilder.buildKey(avatarType);
        final String value = redisTemplate.opsForValue().get(key).block();
        if (StringUtils.isEmpty(value)) {
            return Optional.empty();
        }

        final long epochSeconds = Long.parseLong(value);
        final Instant instant = Instant.ofEpochSecond(epochSeconds);
        final LocalDateTime updatedAt = LocalDateTime.ofInstant(instant, zoneId);
        return Optional.of(updatedAt);
    }

    public List<Pair<AvatarType, LocalDateTime>> mget(
        final List<AvatarType> avatarTypes
    ) {
        Objects.requireNonNull(avatarTypes);

        final List<String> keys = avatarTypes.stream()
            .map(Objects::requireNonNull)
            .map(keyBuilder::buildKey)
            .collect(Collectors.toUnmodifiableList());
        final List<String> values = redisTemplate.opsForValue().multiGet(keys).block();

        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }

        final List<Pair<AvatarType, LocalDateTime>> items = new ArrayList<>();
        for (int i = 0; i < values.size(); ++i) {
            final String value = values.get(i);

            if (StringUtils.isNotEmpty(value)) {
                final AvatarType avatarType = avatarTypes.get(i);
                final long epochSeconds = Long.parseLong(value);
                final Instant instant = Instant.ofEpochSecond(epochSeconds);
                final LocalDateTime updatedAt = LocalDateTime.ofInstant(instant, zoneId);
                items.add(Pair.of(avatarType, updatedAt));
            }
        }

        return List.copyOf(items);
    }
}
