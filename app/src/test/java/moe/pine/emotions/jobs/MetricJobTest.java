package moe.pine.emotions.jobs;

import moe.pine.emotions.mackerel.models.Metric;
import moe.pine.emotions.services.MetricService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MetricJobTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private MetricService metricService;

    @InjectMocks
    private MetricJob metricJob;

    @Test
    public void sendTest() {
        final List<Metric> metrics =
            List.of(
                Metric.builder()
                    .name("metric1")
                    .time(1L)
                    .value(BigDecimal.ONE)
                    .build(),
                Metric.builder()
                    .name("metric2")
                    .time(2L)
                    .value(BigDecimal.valueOf(2L))
                    .build()
            );

        when(metricService.collect()).thenReturn(metrics);
        doNothing().when(metricService).send(metrics);

        metricJob.send();

        verify(metricService).collect();
        verify(metricService).send(metrics);
    }
}
