package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.log.models.AvatarType;
import moe.pine.emotions.log.repositories.AvatarLastUpdatedRepository;
import moe.pine.emotions.models.Metric;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final AvatarLastUpdatedRepository avatarLastUpdatedRepository;
    private final Clock clock;
    private final ZoneId zoneId;

    public void log(
        @Nonnull final AvatarType avatarType
    ) {
        final LocalDateTime now = LocalDateTime.now(clock);
        avatarLastUpdatedRepository.set(avatarType, now);
    }

    public List<Metric> collect() {
        final List<AvatarType> avatarTypes = Arrays.asList(AvatarType.values());
        final List<Pair<AvatarType, LocalDateTime>> items =
            avatarLastUpdatedRepository.mget(avatarTypes);

        final long now = LocalDateTime.now(clock)
            .atZone(zoneId)
            .toEpochSecond();

        return items.stream()
            .map(item -> {
                final long lastUpdatedAt = item.getValue().atZone(zoneId).toEpochSecond();
                final BigDecimal value = BigDecimal.valueOf(now - lastUpdatedAt);
                return Metric.builder()
                    .name("")
                    .time(now)
                    .value(value)
                    .build();
            })
            .collect(Collectors.toUnmodifiableList());
    }
}
