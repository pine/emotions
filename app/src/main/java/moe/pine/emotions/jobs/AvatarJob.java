package moe.pine.emotions.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.log.models.AvatarType;
import moe.pine.emotions.services.CloudStorageService;
import moe.pine.emotions.services.GravatarService;
import moe.pine.emotions.services.MetricService;
import moe.pine.emotions.services.SlackService;
import moe.pine.emotions.services.TwitterService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
@RequiredArgsConstructor
@Slf4j
public class AvatarJob {
    @Nonnull
    private final CloudStorageService cloudStorageService;

    @Nonnull
    private final GravatarService gravatarService;

    @Nonnull
    private final SlackService slackService;

    @Nonnull
    private final TwitterService twitterService;

    @Nonnull
    private final MetricService metricService;

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 0 4 * * *")
    @Retryable
    public void gravatar() {
        gravatarService.chooseImage();
        metricService.log(AvatarType.GRAVATAR);
    }

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 20 4 * * *")
    @Retryable
    public void slack() {
        final byte[] chosenImage = cloudStorageService.chooseImage();
        slackService.updateImage(chosenImage);
        metricService.log(AvatarType.SLACK);
    }

    @ConditionalOnProperty(value = "scheduling.enabled", havingValue = "true")
    @Scheduled(cron = "0 40 4 * * *")
    @Retryable
    public void twitter() {
        final byte[] chosenImage = cloudStorageService.chooseImage();
        twitterService.updateImage(chosenImage);
        metricService.log(AvatarType.TWITTER);
    }
}