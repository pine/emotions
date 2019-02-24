package moe.pine.emotions.jobs;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.Gravatar;
import moe.pine.emotions.properties.GravatarProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AvatarUpdateJob {
    @NonNull
    private final GravatarProperties gravatarProperties;

    @Scheduled(cron = "0 0 5 * * *")
    public void run() throws Exception {
        Gravatar gravatar = new Gravatar(
            gravatarProperties.getEmail(), gravatarProperties.getPassword());

        final List<String> images = gravatarProperties.getImages();
        final List<String> addresses = gravatarProperties.getAddresses();

        gravatar.choiceImage(images, addresses);
    }
}
