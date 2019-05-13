package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.log.models.AvatarType;
import moe.pine.emotions.log.repositories.AvatarUpdatedRepository;
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
public class MetricService {
    private static final String METRICS_NAME_FORMAT = "elapsed_time_%s";

    private final AvatarUpdatedRepository avatarUpdatedRepository;
    private final Clock clock;
    private final ZoneId zoneId;

    public void log(
        @Nonnull final AvatarType avatarType
    ) {
        final LocalDateTime now = LocalDateTime.now(clock);
        avatarUpdatedRepository.set(avatarType, now);
    }

    public List<Metric> collect() {
        final List<AvatarType> avatarTypes = Arrays.asList(AvatarType.values());
        final List<Pair<AvatarType, LocalDateTime>> items =
            avatarUpdatedRepository.mget(avatarTypes);

        final long now = LocalDateTime.now(clock)
            .atZone(zoneId)
            .toEpochSecond();

        return items.stream()
            .map(item -> {
                final String name = String.format(METRICS_NAME_FORMAT, item.getKey().getId());
                final long updatedAt = item.getValue().atZone(zoneId).toEpochSecond();
                final BigDecimal value = BigDecimal.valueOf(now - updatedAt);

                return Metric.builder()
                    .name(name)
                    .time(now)
                    .value(value)
                    .build();
            })
            .collect(Collectors.toUnmodifiableList());
    }
}
