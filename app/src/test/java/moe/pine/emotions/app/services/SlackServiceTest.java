package moe.pine.emotions.app.services;

import moe.pine.emotions.app.properties.SlackProperties;
import moe.pine.emotions.slack.Slack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("ConstantConditions")
public class SlackServiceTest {
    @Mock
    private Slack slack;

    @Mock
    private SlackProperties slackProperties;

    @InjectMocks
    private SlackService slackService;

    @Test
    public void updateImageTest() throws Exception {
        final byte[] imageBytes = {0x00, 0x01, 0x02};
        final List<SlackProperties.Workspace> workspaces =
            List.of(
                new SlackProperties.Workspace("workspace1", "token1"),
                new SlackProperties.Workspace("workspace2", "token2"),
                new SlackProperties.Workspace("workspace3", "token3")
            );

        when(slackProperties.getWorkspaces()).thenReturn(workspaces);
        doNothing().when(slack).setUserPhoto("token1", imageBytes);
        doNothing().when(slack).setUserPhoto("token2", imageBytes);
        doNothing().when(slack).setUserPhoto("token3", imageBytes);

        slackService.updateImage(imageBytes);

        verify(slackProperties, times(1)).getWorkspaces();
        verify(slack, times(1)).setUserPhoto("token1", imageBytes);
        verify(slack, times(1)).setUserPhoto("token2", imageBytes);
        verify(slack, times(1)).setUserPhoto("token3", imageBytes);
        verify(slack, times(3)).setUserPhoto(anyString(), any());
    }

    @Test
    public void updateImageNullImageTest() throws Exception {
        lenient().when(slackProperties.getWorkspaces()).thenReturn(Collections.emptyList());
        lenient().doNothing().when(slack).setUserPhoto(anyString(), any());

        assertThatThrownBy(() -> slackService.updateImage(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("`image` should not be empty.");

        verify(slackProperties, never()).getWorkspaces();
        verify(slack, never()).setUserPhoto(anyString(), any());
    }

    @Test
    @SuppressWarnings("ZeroLengthArrayAllocation")
    public void updateImageEmptyImageTest() throws Exception {
        lenient().when(slackProperties.getWorkspaces()).thenReturn(Collections.emptyList());
        lenient().doNothing().when(slack).setUserPhoto(anyString(), any());

        assertThatThrownBy(() -> slackService.updateImage(new byte[]{}))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageStartingWith("`image` should not be empty.");

        verify(slackProperties, never()).getWorkspaces();
        verify(slack, never()).setUserPhoto(anyString(), any());
    }
}
