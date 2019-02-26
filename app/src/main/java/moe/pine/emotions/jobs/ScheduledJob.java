package moe.pine.emotions.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.services.CloudStorageService;
import moe.pine.emotions.services.GravatarService;
import moe.pine.emotions.services.SlackService;
import moe.pine.emotions.services.TwitterService;
import org.jetbrains.annotations.NotNull;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledJob {
    @NotNull
    private final CloudStorageService cloudStorageService;

    @NotNull
    private final GravatarService gravatarService;

    @NotNull
    private final SlackService slackService;

    @NotNull
    private final TwitterService twitterService;

    @Scheduled(cron = "0 0 4 * * *")
    @Retryable
    public void gravatar() {
        gravatarService.chooseImage();
    }

    @Scheduled(cron = "0 10 4 * * *")
    @Retryable
    public void slack() {
        final byte[] chosenImage = cloudStorageService.chooseImage();
        slackService.updateImage(chosenImage);
    }

    @Scheduled(cron = "0 20 4 * * *")
    @Retryable
    public void twitter() {
        final byte[] chosenImage = cloudStorageService.chooseImage();
        twitterService.updateImage(chosenImage);
    }
}
