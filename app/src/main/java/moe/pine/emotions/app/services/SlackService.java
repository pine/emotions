package moe.pine.emotions.app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.app.properties.SlackProperties;
import moe.pine.emotions.slack.Slack;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackService {
    private final Slack slack;
    private final SlackProperties slackProperties;

    public void updateImage(final byte[] image) throws InterruptedException {
        checkArgument(ArrayUtils.isNotEmpty(image), "`image` should not be empty.");

        for (var workspace : slackProperties.getWorkspaces()) {
            slack.setUserPhoto(workspace.getToken(), image);
            log.info("Updated Slack user photo. [workspace.id={}]", workspace.getId());
        }
    }
}
