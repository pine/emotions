package moe.pine.emotions.mackerel;

import moe.pine.emotions.mackerel.models.Metric;
import moe.pine.emotions.mackerel.models.Status;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static moe.pine.emotions.mackerel.Mackerel.API_KEY_HEADER;
import static moe.pine.emotions.mackerel.Mackerel.ENDPOINT;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MackerelTest {
    private static final String API_KEY = "API_KEY";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private Mackerel mackerel;

    @Captor
    private ArgumentCaptor<HttpEntity<?>> entityCaptor;

    @Before
    public void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        mackerel = new Mackerel(restTemplateBuilder, API_KEY);
    }

    @Test
    public void sendTest() {
        final List<Metric> metrics =
            List.of(
                Metric.builder()
                    .name("metric-1")
                    .time(1L)
                    .value(BigDecimal.ONE)
                    .build(),
                Metric.builder()
                    .name("metric-2")
                    .time(2L)
                    .value(BigDecimal.valueOf(2L))
                    .build());
        final Status status = Status.builder().success(true).build();

        when(restTemplate.postForObject(eq(ENDPOINT), entityCaptor.capture(), eq(Status.class)))
            .thenReturn(status);

        mackerel.send(metrics);

        verify(restTemplate).postForObject(eq(ENDPOINT), any(), eq(Status.class));

        final HttpEntity<?> entity = entityCaptor.getValue();
        final HttpHeaders headers = entity.getHeaders();
        assertEquals(MediaType.APPLICATION_JSON_UTF8, headers.getContentType());
        assertEquals(API_KEY, headers.getFirst(API_KEY_HEADER));
        assertEquals(metrics, entity.getBody());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void sendTest_nullMetrics() {
        expectedException.expect(NullPointerException.class);
        mackerel.send(null);
    }

    @Test
    public void sendTest_emptyMetrics() {
        mackerel.send(Collections.emptyList());

        verify(restTemplate, never()).postForObject(anyString(), any(), any());
    }

    @Test
    public void sendTest_nullResponse() {
        expectedException.expect(MackerelException.class);
        expectedException.expectMessage("Failed to call Mackerel API. An empty response received.");

        final List<Metric> metrics =
            List.of(
                Metric.builder()
                    .name("metric-1")
                    .time(1L)
                    .value(BigDecimal.ONE)
                    .build());

        when(restTemplate.postForObject(eq(ENDPOINT), any(), eq(Status.class)))
            .thenReturn(null);

        mackerel.send(metrics);

        verify(restTemplate).postForObject(eq(ENDPOINT), any(), eq(Status.class));
    }

    @Test
    public void sendTest_failedResponse() {
        expectedException.expect(MackerelException.class);
        expectedException.expectMessage("Failed to call Mackerel API :: success=false, metrics=");

        final List<Metric> metrics =
            List.of(
                Metric.builder()
                    .name("metric-1")
                    .time(1L)
                    .value(BigDecimal.ONE)
                    .build());
        final Status status = Status.builder().success(false).build();

        when(restTemplate.postForObject(eq(ENDPOINT), any(), eq(Status.class)))
            .thenReturn(status);

        mackerel.send(metrics);

        verify(restTemplate).postForObject(eq(ENDPOINT), any(), eq(Status.class));
    }
}
