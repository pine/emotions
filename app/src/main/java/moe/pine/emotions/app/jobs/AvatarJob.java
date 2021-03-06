package moe.pine.emotions.app.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.log.AvatarType;
import moe.pine.emotions.app.services.BookmeterService;
import moe.pine.emotions.app.services.CloudStorageService;
import moe.pine.emotions.app.services.GravatarService;
import moe.pine.emotions.app.services.MetricService;
import moe.pine.emotions.app.services.SlackService;
import moe.pine.emotions.app.services.TwitterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AvatarJob {
    private final BookmeterService bookmeterService;
    private final CloudStorageService cloudStorageService;
    private final GravatarService gravatarService;
    private final SlackService slackService;
    private final TwitterService twitterService;
    private final MetricService metricService;

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 0 4 * * *")
    @Retryable
    public void bookmeter() throws InterruptedException {
        byte[] chosenImage = cloudStorageService.chooseImage();
        bookmeterService.updateImage(chosenImage);
        metricService.log(AvatarType.BOOKMETER);
    }

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 10 4 * * *")
    @Retryable
    public void gravatar() {
        gravatarService.chooseImage();
        metricService.log(AvatarType.GRAVATAR);
    }

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 20 4 * * *")
    @Retryable
    public void slack() throws InterruptedException {
        byte[] chosenImage = cloudStorageService.chooseImage();
        slackService.updateImage(chosenImage);
        metricService.log(AvatarType.SLACK);
    }

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 30 4 * * *")
    @Retryable
    public void twitter() {
        byte[] chosenImage = cloudStorageService.chooseImage();
        twitterService.updateImage(chosenImage);
        metricService.log(AvatarType.TWITTER);
    }
}
