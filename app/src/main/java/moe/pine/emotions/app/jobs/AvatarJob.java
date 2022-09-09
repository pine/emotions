package moe.pine.emotions.app.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.app.services.BookmeterService;
import moe.pine.emotions.app.services.CloudStorageService;
import moe.pine.emotions.app.services.GravatarService;
import moe.pine.emotions.app.services.TwitterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;

@RequiredArgsConstructor
@Slf4j
public class AvatarJob {
    private final BookmeterService bookmeterService;
    private final CloudStorageService cloudStorageService;
    private final GravatarService gravatarService;
    private final TwitterService twitterService;

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 0 4 * * *")
    @Retryable
    public void bookmeter() throws InterruptedException {
        byte[] chosenImage = cloudStorageService.chooseImage();
        bookmeterService.updateImage(chosenImage);
    }

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 10 4 * * *")
    @Retryable
    public void gravatar() {
        gravatarService.chooseImage();
    }

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 30 4 * * *")
    @Retryable
    public void twitter() {
        byte[] chosenImage = cloudStorageService.chooseImage();
        twitterService.updateImage(chosenImage);
    }
}
