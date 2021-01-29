package moe.pine.emotions.mackerel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static moe.pine.emotions.mackerel.Mackerel.TIMEOUT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("ConstantConditions")
public class MackerelTest {
    private static final String API_KEY = "API_KEY";

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private Mackerel mackerel;

    @Captor
    private ArgumentCaptor<HttpEntity<?>> entityCaptor;

    @BeforeEach
    public void setUp() {
        when(restTemplateBuilder.setConnectTimeout(TIMEOUT)).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.setReadTimeout(TIMEOUT)).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        mackerel = new Mackerel(restTemplateBuilder, API_KEY);
    }

    @Test
    public void sendTest() {
        final List<Metric> metrics =
            List.of(
                new Metric("metric-1", 1L, BigDecimal.ONE),
                new Metric("metric-2", 2L, BigDecimal.valueOf(2L))
            );
        final Status status = Status.builder().success(true).build();

        when(restTemplate.postForObject(eq(ENDPOINT), entityCaptor.capture(), eq(Status.class)))
            .thenReturn(status);

        mackerel.send(metrics);

        verify(restTemplate).postForObject(eq(ENDPOINT), any(), eq(Status.class));

        final HttpEntity<?> entity = entityCaptor.getValue();
        final HttpHeaders headers = entity.getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals(API_KEY, headers.getFirst(API_KEY_HEADER));
        assertEquals(metrics, entity.getBody());
    }

    @Test
    public void sendTest_nullMetrics() {
        assertThrows(NullPointerException.class, () -> mackerel.send(null));
    }

    @Test
    public void sendTest_emptyMetrics() {
        mackerel.send(Collections.emptyList());

        verify(restTemplate, never()).postForObject(anyString(), any(), any());
    }

    @Test
    public void sendTest_nullResponse() {
        final List<Metric> metrics =
            List.of(new Metric("metric-1", 1L, BigDecimal.ONE));

        when(restTemplate.postForObject(eq(ENDPOINT), any(), eq(Status.class)))
            .thenReturn(null);

        final Exception exception = assertThrows(MackerelException.class, () -> mackerel.send(metrics));
        assertEquals("Failed to call Mackerel API. An empty response received.", exception.getMessage());

        verify(restTemplate).postForObject(eq(ENDPOINT), any(), eq(Status.class));
    }

    @Test
    public void sendTest_failedResponse() {
        final List<Metric> metrics = List.of(new Metric("metric-1", 1L, BigDecimal.ONE));
        final Status status = Status.builder().success(false).build();

        when(restTemplate.postForObject(anyString(), any(), eq(Status.class))).thenReturn(status);

        final Exception exception = assertThrows(MackerelException.class, () -> mackerel.send(metrics));
        assertTrue(exception.getMessage().startsWith("Failed to call Mackerel API :: success=false, metrics="));

        verify(restTemplate).postForObject(eq(ENDPOINT), any(), eq(Status.class));
    }
}
