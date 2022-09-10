package moe.pine.emotions.app.batch;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.app.services.GravatarService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GravatarTasklet implements Tasklet {
    private final GravatarService gravatarService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        gravatarService.chooseImage();

        return RepeatStatus.FINISHED;
    }
}
