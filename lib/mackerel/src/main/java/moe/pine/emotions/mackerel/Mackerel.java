package moe.pine.emotions.mackerel;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.mackerel.models.Metric;
import moe.pine.emotions.mackerel.models.Status;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@RequiredArgsConstructor
public class Mackerel {
    static final String ENDPOINT = "https://api.mackerelio.com/api/v0/services/app/tsdb";
    static final String API_KEY_HEADER = "X-Api-Key";

    private final RestTemplate restTemplate;
    private final String apiKey;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void send(
        @Nonnull final List<Metric> metrics
    ) {
        checkNotNull(metrics);

        if (CollectionUtils.isEmpty(metrics)) {
            return;
        }

        final var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
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
