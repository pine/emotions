package moe.pine.emotions.jobs;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.mackerel.models.Metric;
import moe.pine.emotions.services.MetricService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricJob {
    @Nonnull
    private final MetricService metricService;

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 * * * * *")
    @Retryable
    public void mackerel() {
        final List<Metric> metrics = metricService.collect();
        metricService.send(metrics);
    }
}
