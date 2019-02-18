package moe.pine.emotions.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AvatarUpdateJob {
    @Scheduled(cron = "* * * * * *")
    public void run() {
    }
}
