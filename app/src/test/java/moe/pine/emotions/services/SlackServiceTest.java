package moe.pine.emotions.services;

import lombok.SneakyThrows;
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

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class SlackServiceTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Slack slack;

    @Mock
    private SlackProperties slackProperties;

    @InjectMocks
    private SlackService slackService;

    @Test
    @SneakyThrows
    public void updateImageTest() {
        final byte[] imageBytes = new byte[]{0x00, 0x01, 0x02};
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
    @SneakyThrows
    @SuppressWarnings("ConstantConditions")
    public void updateImageNullImageTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        when(slackProperties.getWorkspaces()).thenReturn(Collections.emptyList());
        doNothing().when(slack).setUserPhoto(anyString(), any());

        slackService.updateImage(null);

        verify(slackProperties, never()).getWorkspaces();
        verify(slack, never()).setUserPhoto(anyString(), any());
    }

    @Test
    @SneakyThrows
    public void updateImageEmptyImageTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        when(slackProperties.getWorkspaces()).thenReturn(Collections.emptyList());
        doNothing().when(slack).setUserPhoto(anyString(), any());

        slackService.updateImage(new byte[]{});

        verify(slackProperties, never()).getWorkspaces();
        verify(slack, never()).setUserPhoto(anyString(), any());
    }
}
