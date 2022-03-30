package moe.pine.emotions.mackerel;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

public class Mackerel {
    static final String ENDPOINT = "https://api.mackerelio.com/api/v0/services/app/tsdb";
    static final String API_KEY_HEADER = "X-Api-Key";
    static final Duration TIMEOUT = Duration.ofSeconds(60);

    private final RestTemplate restTemplate;
    private final String apiKey;

    public Mackerel(
        final RestTemplateBuilder restTemplateBuilder,
        final String apiKey
    ) {
        this.restTemplate = restTemplateBuilder
            .setConnectTimeout(TIMEOUT)
            .setReadTimeout(TIMEOUT)
            .build();
        this.apiKey = Objects.requireNonNull(apiKey);
    }

    public void send(
        final List<Metric> metrics
    ) {
        Objects.requireNonNull(metrics);

        if (CollectionUtils.isEmpty(metrics)) {
            return;
        }

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(API_KEY_HEADER, apiKey);

        final HttpEntity<?> request = new HttpEntity<>(metrics, headers);
        final Status status = restTemplate.postForObject(ENDPOINT, request, Status.class);
        if (status == null) {
            throw new MackerelException("Failed to call Mackerel API. An empty response received.");
        }
        if (!status.isSuccess()) {
            throw new MackerelException(
                String.format(
                    "Failed to call Mackerel API :: success=%s, metrics=%s",
                    String.valueOf(status.isSuccess()),
                    metrics.toString()));
        }
    }
}
