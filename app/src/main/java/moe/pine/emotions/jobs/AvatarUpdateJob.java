package moe.pine.emotions.jobs;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.properties.GravatarProperties;
import moe.pine.emotions.properties.SlackProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AvatarUpdateJob {
    @NonNull
    private final GravatarProperties gravatarProperties;

    @NonNull
    private final SlackProperties slackProperties;

    @Scheduled(cron = "* * * * * *")
    public void run() throws Exception {
        log.info(slackProperties.getChannels().toString());

        /*
        Gravatar gravatar = new Gravatar(
            gravatarProperties.getEmail(), gravatarProperties.getPassword());

        final List<String> images = gravatarProperties.getImages();
        final List<String> addresses = gravatarProperties.getAddresses();

        gravatar.choiceImage(images, addresses);
        */
    }
}
