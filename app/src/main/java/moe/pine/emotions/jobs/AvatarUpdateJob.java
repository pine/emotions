package moe.pine.emotions.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.settings.GravatarSetting;
import moe.pine.emotions.gravatar.Gravatar;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AvatarUpdateJob {
    private final GravatarSetting gravatarConfig;

    @Scheduled(cron = "* * * * * *")
    public void run() {
        Gravatar gravatar = new Gravatar("", "");
        gravatar.choiceImage();
    }
}
