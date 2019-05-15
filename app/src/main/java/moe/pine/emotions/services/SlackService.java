package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.properties.SlackProperties;
import moe.pine.emotions.slack.Slack;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackService {
    @Nonnull
    private final Slack slack;

    @Nonnull
    private final SlackProperties slackProperties;

    public void updateImage(@Nonnull final byte[] image) {
        checkArgument(ArrayUtils.isNotEmpty(image), "`image` should not be empty.");

        slackProperties.getWorkspaces()
            .forEach(workspace -> slack.setUserPhoto(workspace.getToken(), image));
    }
}
