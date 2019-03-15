package moe.pine.emotions.slack;

import moe.pine.emotions.slack.models.Status;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class SlackTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private Slack slack;

    @Test
    public void constructorTest() {
        final Slack slack = new Slack();
        final RestTemplate restTemplate = Whitebox.getInternalState(slack, "restTemplate");
        assertNotNull(restTemplate);
    }

    @Test
    public void setUserPhotoTest() {
        final Status response = new Status() {{
            setOk(true);
        }};

        when(
            restTemplate.postForObject(
                eq(Slack.SLACK_USERS_SET_PHOTO), any(), eq(Status.class))
        ).thenReturn(response);

        slack.setUserPhoto("token", new byte[]{0x00});

        verify(restTemplate).postForObject(anyString(), any(), eq(Status.class));
    }

    @Test
    public void setUserPhotoEmptyTokenTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`token` should not be empty.");

        slack.setUserPhoto("", new byte[]{0x00});
    }

    @Test
    public void setUserPhotoEmptyImageTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        slack.setUserPhoto("token", new byte[]{});
    }
}
