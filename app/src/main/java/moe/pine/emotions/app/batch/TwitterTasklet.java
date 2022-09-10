package moe.pine.emotions.app.batch;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.app.services.CloudStorageService;
import moe.pine.emotions.twitter.Twitter;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TwitterTasklet implements Tasklet {
    private final CloudStorageService cloudStorageService;
    private final Twitter twitter;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final var chosenImage = cloudStorageService.chooseImage();
        twitter.updateProfileImage(chosenImage);

        return RepeatStatus.FINISHED;
    }
}
