package moe.pine.emotions.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.GravatarException;
import moe.pine.emotions.services.GravatarService;
import org.jetbrains.annotations.NotNull;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledJob {
    @NotNull
    private final GravatarService gravatarService;

    @Scheduled(cron = "0 0 5 * * *")
    @Retryable
    public void gravatar() throws GravatarException {
        gravatarService.choiceImage();
    }
}
