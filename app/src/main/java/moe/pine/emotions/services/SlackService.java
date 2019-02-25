package moe.pine.emotions.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.properties.SlackProperties;
import moe.pine.emotions.slack.Slack;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlackService {
    @NotNull
    private final Slack slack;

    @NotNull
    private final SlackProperties slackProperties;

    public void updateImage() {
        for (SlackProperties.Channel channel : slackProperties.getChannels()) {
            final Resource resource = new ByteArrayResource(new byte[]{});
            slack.setUserPhoto(channel.getToken(), resource);
        }
    }
}
