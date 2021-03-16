package moe.pine.emotions.app.services;

import moe.pine.emotions.twitter.Twitter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TwitterServiceTest {
    @Mock
    private Twitter twitter;

    @InjectMocks
    private TwitterService twitterService;

    @Test
    public void updateImageTest() {
        final byte[] imageBytes = {0x00, 0x01, 0x02};
        doNothing().when(twitter).updateProfileImage(imageBytes);

        twitterService.updateImage(imageBytes);

        verify(twitter, times(1)).updateProfileImage(imageBytes);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void updateImageNullImageTest() {
        lenient().doNothing().when(twitter).updateProfileImage(any());

        assertThatThrownBy(() -> twitterService.updateImage(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("`image` should not be empty.");

        verify(twitter, never()).updateProfileImage(any());
    }

    @Test
    @SuppressWarnings("ZeroLengthArrayAllocation")
    public void updateImageEmptyImageTest() {
        lenient().doNothing().when(twitter).updateProfileImage(any());

        assertThatThrownBy(() -> twitterService.updateImage(new byte[]{}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("`image` should not be empty.");

        verify(twitter, never()).updateProfileImage(any());
    }
}
