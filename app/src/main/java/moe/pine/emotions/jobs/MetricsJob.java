package moe.pine.emotions.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.models.Metric;
import moe.pine.emotions.services.MackerelService;
import moe.pine.emotions.services.MetricsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MetricsJob {
    @Nonnull
    private final MetricsService metricsService;

    @Nonnull
    private final MackerelService mackerelService;

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 * * * * *")
    @Retryable
    public void mackerel() {
        final List<Metric> metrics = metricsService.collect();
        log.debug("The metrics collected :: {}", metrics);

        mackerelService.send(metrics);
    }
}
