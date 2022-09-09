package moe.pine.emotions.app.jobs;

import moe.pine.emotions.mackerel.Metric;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MetricJobTest {
    @Mock
    private MetricService metricService;

    @InjectMocks
    private MetricJob metricJob;

    @Test
    public void sendTest() {
        final List<Metric> metrics =
            List.of(
                new Metric("metric1", 1L, BigDecimal.ONE),
                new Metric("metric2", 2L, BigDecimal.valueOf(2L))
            );

        when(metricService.collect()).thenReturn(metrics);
        doNothing().when(metricService).send(metrics);

        metricJob.send();

        verify(metricService).collect();
        verify(metricService).send(metrics);
    }
}
