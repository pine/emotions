package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.properties.SlackProperties;
import moe.pine.emotions.slack.Slack;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import static com.google.common.base.Preconditions.checkArgument;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackService {
    @NotNull
    private final Slack slack;

    @NotNull
    private final SlackProperties slackProperties;

    public void updateImage(@NotNull final byte[] image) {
        checkArgument(ArrayUtils.isNotEmpty(image), "`image` should not be empty.");

        slackProperties.getChannels()
            .forEach(channel -> slack.setUserPhoto(channel.getToken(), image));
    }
}
