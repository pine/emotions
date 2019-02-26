package moe.pine.emotions.slack;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.slack.models.Status;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.web.client.RestTemplate;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class Slack {
    @SuppressWarnings("WeakerAccess")
    public static final String SLACK_USERS_SET_PHOTO = "https://slack.com/api/users.setPhoto";

    @NotNull
    private final RestTemplate restTemplate;

    public Slack() {
        this(new RestTemplate());
    }

    @VisibleForTesting
    Slack(@NotNull final RestTemplate restTemplate) {
        this.restTemplate = checkNotNull(restTemplate);
    }

    public void setUserPhoto(
        @NotNull final String token,
        @NotNull final byte[] image
    ) {
        checkArgument(StringUtils.isNotEmpty(token), "`token` should not be empty.");
        checkArgument(ArrayUtils.isNotEmpty(image), "`image` should not be empty.");

        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

        final MultipartBodyBuilder body = new MultipartBodyBuilder();
        final Resource resource = new ByteArrayResource(image);
        body.part("image", resource, MediaType.IMAGE_PNG);

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
