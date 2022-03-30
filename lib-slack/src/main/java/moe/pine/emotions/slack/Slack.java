package moe.pine.emotions.slack;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.spring_utils.NamedByteArrayResource;
import moe.pine.reactor.interruptible.MonoUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class Slack {
    private static final Duration BLOCK_TIMEOUT = Duration.ofSeconds(60);

    static final String BASE_URL = "https://slack.com";
    static final String USERS_SET_PHOTO_PATH = "/api/users.setPhoto";

    private final WebClient webClient;

    public Slack(final WebClient.Builder webClientBuilder) {
        this(webClientBuilder, BASE_URL);
    }

    Slack(
        final WebClient.Builder webClientBuilder,
        final String baseUrl
    ) {
        webClient = webClientBuilder.baseUrl(baseUrl).build();
    }

    public void setUserPhoto(
        final String token,
        final byte[] image
    ) throws InterruptedException {
        checkArgument(StringUtils.isNotEmpty(token), "`token` should not be empty.");
        checkArgument(ArrayUtils.isNotEmpty(image), "`image` should not be empty.");

        final var body = new MultipartBodyBuilder();
        final Resource resource = new NamedByteArrayResource(image, "image.png");
        body.part("image", resource, MediaType.IMAGE_PNG);

        final Status status = MonoUtils
            .blockOptional(
                webClient.post()
                    .uri(USERS_SET_PHOTO_PATH)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .bodyValue(body.build())
                    .retrieve()
                    .bodyToMono(Status.class), BLOCK_TIMEOUT)
            .orElseThrow(() -> new SlackException(
                "Failed to call users.setPhoto API. An empty response received."));

        if (!status.isOk()) {
            throw new SlackException(String.format(
                "Failed to call users.setPhoto API :: ok=%s, error=%s",
                status.isOk(),
                status.getError()));
        }
    }
}
