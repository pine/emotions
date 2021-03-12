package moe.pine.emotions.app.services;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.log.AvatarType;
import moe.pine.emotions.log.AvatarUpdatedRepository;
import moe.pine.emotions.mackerel.Mackerel;
import moe.pine.emotions.mackerel.Metric;
import moe.pine.emotions.app.properties.MackerelProperties;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricService {
    private final AvatarUpdatedRepository avatarUpdatedRepository;
    private final Mackerel mackerel;
    private final MackerelProperties mackerelProperties;
    private final Clock clock;
    private final ZoneId zoneId;

    public void log(final AvatarType avatarType) {
        Objects.requireNonNull(avatarType);

        final LocalDateTime now = LocalDateTime.now(clock);
        avatarUpdatedRepository.set(avatarType, now);
    }

    public List<Metric> collect() {
        final List<AvatarType> avatarTypes = List.of(AvatarType.values());
        final List<Pair<AvatarType, LocalDateTime>> items =
            avatarUpdatedRepository.mget(avatarTypes);

        final long now = clock.instant().getEpochSecond();
        final String graphName = mackerelProperties.getGraphs().getElapsedTime().getName();
        return items.stream()
            .map(item -> {
                final String name = String.format("%s.%s", graphName, item.getKey().getId());
                final long updatedAt = item.getValue().atZone(zoneId).toEpochSecond();
                final BigDecimal value = BigDecimal.valueOf(now - updatedAt);

                return new Metric(name, now, value);
            })
            .collect(Collectors.toUnmodifiableList());
    }

    public void send(final List<Metric> metrics) {
        Objects.requireNonNull(metrics);

        mackerel.send(metrics);
    }
}
