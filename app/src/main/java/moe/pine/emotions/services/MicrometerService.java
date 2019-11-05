package moe.pine.emotions.services;

import com.google.common.annotations.VisibleForTesting;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.log.AvatarType;
import moe.pine.emotions.log.AvatarUpdatedRepository;
import moe.pine.emotions.properties.MicrometerProperties;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;


@Slf4j
@Component
public class MicrometerService {
    private final AvatarUpdatedRepository avatarUpdatedRepository;
    private final Clock clock;
    private final ZoneId zoneId;

    public MicrometerService(
        final AvatarUpdatedRepository avatarUpdatedRepository,
        final Clock clock,
        final ZoneId zoneId,
        final MicrometerProperties micrometerProperties,
        final MeterRegistry meterRegistry
    ) {
        this.avatarUpdatedRepository = avatarUpdatedRepository;
        this.clock = clock;
        this.zoneId = zoneId;

        final MicrometerProperties.Metric metric =
            micrometerProperties.getMetrics().getElapsedTime();

        for (var avatarType : AvatarType.values()) {
            final String name = String.format("%s.%s", metric.getName(), avatarType.getId());
            Gauge.builder(name, () -> measure(avatarType).orElse(null))
                .register(meterRegistry);
        }
    }


    @VisibleForTesting
    Optional<Long> measure(final AvatarType avatarType) {
        final Optional<LocalDateTime> updatedAtOpt = avatarUpdatedRepository.get(avatarType);
        if (updatedAtOpt.isEmpty()) {
            return Optional.empty();
        }

        final LocalDateTime updatedAt = updatedAtOpt.get();
        final long updatedAtEpoch = updatedAt.atZone(zoneId).toEpochSecond();
        final long now = clock.instant().getEpochSecond();
        return Optional.of(now - updatedAtEpoch);
    }
}
