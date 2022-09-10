package moe.pine.emotions.app.jobs;

import moe.pine.emotions.app.services.BookmeterService;
import moe.pine.emotions.app.services.CloudStorageService;
import moe.pine.emotions.app.services.GravatarService;
import moe.pine.emotions.log.AvatarType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AvatarJobTest {
    @Mock
    private BookmeterService bookmeterService;

    @Mock
    private CloudStorageService cloudStorageService;

    @Mock
    private GravatarService gravatarService;

    @Mock
    private SlackService slackService;

    @Mock
    private TwitterService twitterService;

    @Mock
    private MetricService metricService;

    @InjectMocks
    private AvatarJob avatarJob;

    @Captor
    private ArgumentCaptor<byte[]> imageBytesCaptor;

    @Test
    public void bookmeterTest() throws Exception {
        final byte[] imageBytes = {0x01, 0x02, 0x03};

        when(cloudStorageService.chooseImage()).thenReturn(imageBytes);
        doNothing().when(bookmeterService).updateImage(imageBytes);
        doNothing().when(metricService).log(AvatarType.BOOKMETER);

        avatarJob.bookmeter();

        verify(bookmeterService).updateImage(imageBytes);
        verify(metricService).log(AvatarType.BOOKMETER);
    }

    @Test
    public void gravatarTest() {
        doNothing().when(gravatarService).chooseImage();
        doNothing().when(metricService).log(AvatarType.GRAVATAR);

        avatarJob.gravatar();

        verify(gravatarService).chooseImage();
        verify(metricService).log(AvatarType.GRAVATAR);
    }

    @Test
    public void slackTest() throws Exception {
        final byte[] imageBytes = {0x01, 0x02, 0x03};

        when(cloudStorageService.chooseImage()).thenReturn(imageBytes);
        doNothing().when(slackService).updateImage(imageBytesCaptor.capture());
        doNothing().when(metricService).log(AvatarType.SLACK);

        avatarJob.slack();

        verify(cloudStorageService).chooseImage();
        verify(slackService).updateImage(any());
        verify(metricService).log(AvatarType.SLACK);

        assertArrayEquals(imageBytes, imageBytesCaptor.getValue());
    }

    @Test
    public void twitterTest() {
        final byte[] imageBytes = {0x01, 0x02, 0x03};

        when(cloudStorageService.chooseImage()).thenReturn(imageBytes);
        doNothing().when(twitterService).updateImage(imageBytesCaptor.capture());
        doNothing().when(metricService).log(AvatarType.TWITTER);

        avatarJob.twitter();

        verify(cloudStorageService).chooseImage();
        verify(twitterService).updateImage(any());
        verify(metricService).log(AvatarType.TWITTER);

        assertArrayEquals(imageBytes, imageBytesCaptor.getValue());
    }
}
