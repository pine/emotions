package moe.pine.emotions.slack;

import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.slack.models.Status;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class Slack {
    private static final String SLACK_USERS_SET_PHOTO = "https://slack.com/api/users.setPhoto";

    private final RestTemplate restTemplate;

    public Slack() {
        this(new RestTemplate());
    }

    public Slack(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setUserPhoto(
        @NotNull final String token,
        @NotNull final Resource image
    ) throws SlackException {
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        final MultipartBodyBuilder body = new MultipartBodyBuilder();
        body.part("image", image, MediaType.IMAGE_PNG);

        final HttpEntity<?> request = new HttpEntity<>(body.build(), headers);
        final Status status = restTemplate.postForObject(SLACK_USERS_SET_PHOTO, request, Status.class);

        if (status == null) {
            throw new SlackException("Failed to call users.setPhoto API. An empty response received.");
        }
        if (!status.isOk()) {
            throw new SlackException(String.format(
                "Failed to call users.setPhoto API :: ok=%s, error=%s",
                String.valueOf(status.isOk()),
                String.valueOf(status.getError())));
        }
    }
}
