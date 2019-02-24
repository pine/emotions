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

    @Scheduled(cron = "* * * * * *")
    public void run() {
        Gravatar gravatar = new Gravatar(
            gravatarProperties.getEmail(), gravatarProperties.getPassword());

        List<String> images = gravatarProperties.getImages();

        gravatar.choiceImage();
    }
}
