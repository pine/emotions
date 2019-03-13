package moe.pine.emotions.services;

import com.google.common.collect.ImmutableList;
import moe.pine.emotions.properties.SlackProperties;
import moe.pine.emotions.slack.Slack;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class SlackServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    Slack slack;

    @Mock
    SlackProperties slackProperties;

    @InjectMocks
    SlackService slackService;

    @Test
    public void updateImageTest() {
        final byte[] imageBytes = new byte[]{0x00, 0x01, 0x02};
        final List<SlackProperties.Channel> channels =
            ImmutableList.of(
                new SlackProperties.Channel("workspace1", "token1"),
                new SlackProperties.Channel("workspace2", "token2"),
                new SlackProperties.Channel("workspace3", "token3")
            );

        when(slackProperties.getChannels()).thenReturn(channels);
        doNothing().when(slack).setUserPhoto("token1", imageBytes);
        doNothing().when(slack).setUserPhoto("token2", imageBytes);
        doNothing().when(slack).setUserPhoto("token3", imageBytes);

        slackService.updateImage(imageBytes);

        verify(slackProperties, times(1)).getChannels();
        verify(slack, times(1)).setUserPhoto("token1", imageBytes);
        verify(slack, times(1)).setUserPhoto("token2", imageBytes);
        verify(slack, times(1)).setUserPhoto("token3", imageBytes);
        verify(slack, times(3)).setUserPhoto(anyString(), any());
    }

    @Test
    public void updateImageNullImageTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        when(slackProperties.getChannels()).thenReturn(Collections.emptyList());
        doNothing().when(slack).setUserPhoto(anyString(), any());

        //noinspection ConstantConditions
        slackService.updateImage(null);

        verify(slackProperties, never()).getChannels();
        verify(slack, never()).setUserPhoto(anyString(), any());
    }

    @Test
    public void updateImageEmptyImageTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        when(slackProperties.getChannels()).thenReturn(Collections.emptyList());
        doNothing().when(slack).setUserPhoto(anyString(), any());

        slackService.updateImage(new byte[]{});

        verify(slackProperties, never()).getChannels();
        verify(slack, never()).setUserPhoto(anyString(), any());
    }
}