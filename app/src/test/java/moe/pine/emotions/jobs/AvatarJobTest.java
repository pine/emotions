package moe.pine.emotions.jobs;

import moe.pine.emotions.services.CloudStorageService;
import moe.pine.emotions.services.GravatarService;
import moe.pine.emotions.services.SlackService;
import moe.pine.emotions.services.TwitterService;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AvatarJobTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private CloudStorageService cloudStorageService;

    @Mock
    private GravatarService gravatarService;

    @Mock
    private SlackService slackService;

    @Mock
    private TwitterService twitterService;

    @InjectMocks
    private AvatarJob avatarJob;

    @Captor
    private ArgumentCaptor<byte[]> imageBytesCaptor;

    @Test
    public void gravatarTest() {
        doNothing().when(gravatarService).chooseImage();

        avatarJob.gravatar();

        verify(gravatarService).chooseImage();
    }

    @Test
    public void slackTest() {
        final byte[] imageBytes = new byte[]{0x01, 0x02, 0x03};

        when(cloudStorageService.chooseImage()).thenReturn(imageBytes);
        doNothing().when(slackService).updateImage(imageBytesCaptor.capture());

        avatarJob.slack();

        verify(cloudStorageService).chooseImage();
        verify(slackService).updateImage(any());
        assertArrayEquals(imageBytes, imageBytesCaptor.getValue());
    }

    @Test
    public void twitterTest() {
        final byte[] imageBytes = new byte[]{0x01, 0x02, 0x03};

        when(cloudStorageService.chooseImage()).thenReturn(imageBytes);
        doNothing().when(twitterService).updateImage(imageBytesCaptor.capture());

        avatarJob.twitter();

        verify(cloudStorageService).chooseImage();
        verify(twitterService).updateImage(any());
        assertArrayEquals(imageBytes, imageBytesCaptor.getValue());
    }
}
