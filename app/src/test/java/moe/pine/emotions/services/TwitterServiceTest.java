package moe.pine.emotions.services;

import moe.pine.emotions.twitter.Twitter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.mockito.Mockito.*;

public class TwitterServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    Twitter twitter;

    @InjectMocks
    TwitterService twitterService;

    @Test
    public void updateImageTest() {
        final byte[] imageBytes = new byte[]{0x00, 0x01, 0x02};
        doNothing().when(twitter).updateProfileImage(imageBytes);

        twitterService.updateImage(imageBytes);

        verify(twitter, times(1)).updateProfileImage(imageBytes);
    }

    @Test
    public void updateImageNullImageTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        doNothing().when(twitter).updateProfileImage(any());

        //noinspection ConstantConditions
        twitterService.updateImage(null);

        verify(twitter, never()).updateProfileImage(any());
    }

    @Test
    public void updateImageEmptyImageTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        doNothing().when(twitter).updateProfileImage(any());

        twitterService.updateImage(new byte[]{});

        verify(twitter, never()).updateProfileImage(any());
    }
}
