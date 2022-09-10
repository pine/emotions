package moe.pine.emotions.app.batch;

import lombok.RequiredArgsConstructor;
import moe.pine.emotions.app.services.BookmeterService;
import moe.pine.emotions.app.services.CloudStorageService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmeterTasklet implements Tasklet {
    private final BookmeterService bookmeterService;
    private final CloudStorageService cloudStorageService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        final byte[] chosenImage = cloudStorageService.chooseImage();
        bookmeterService.updateImage(chosenImage);

        return RepeatStatus.FINISHED;
    }
}
