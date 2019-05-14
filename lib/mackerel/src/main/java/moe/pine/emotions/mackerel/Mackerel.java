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
    private static final String ENDPOINT = "https://api.mackerelio.com/api/v0/services/app/tsdb";
    private static final String API_KEY_HEADER = "X-Api-Key";

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
        restTemplate.postForObject(ENDPOINT, request, Status.class);
    }
}
