package moe.pine.emotions.app.jobs;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.mackerel.Metric;
import moe.pine.emotions.app.services.MetricService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MetricJob {
    private final MetricService metricService;

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 * * * * *")
    @Retryable
    public void send() {
        List<Metric> metrics = metricService.collect();
        metricService.send(metrics);
    }
}
