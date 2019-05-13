package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.log.models.AvatarType;
import moe.pine.emotions.log.repositories.AvatarLastUpdatedLogRepository;
import moe.pine.emotions.models.Metric;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MetricsService {
    private final AvatarLastUpdatedLogRepository avatarLastUpdatedLogRepository;
    private final Clock clock;

    public void log(
        @Nonnull final AvatarType avatarType
    ) {
        final LocalDateTime now = LocalDateTime.now(clock);
        avatarLastUpdatedLogRepository.update(avatarType, now);
    }

    public List<Metric> get() {
        return Collections.emptyList();
    }
}
