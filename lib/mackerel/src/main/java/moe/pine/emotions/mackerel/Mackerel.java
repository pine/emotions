package moe.pine.emotions.mackerel;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@RequiredArgsConstructor
public class Mackerel {
    @Nonnull
    private final RestTemplate restTemplate;

    @Nonnull
    private final String apiKey;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void send(
        @Nonnull final List<Metric> metrics
    ) {
        checkNotNull(metrics);

        if (CollectionUtils.isEmpty(metrics)) {
            return;
        }

    }
}
