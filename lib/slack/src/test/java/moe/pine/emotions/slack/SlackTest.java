package moe.pine.emotions.slack;

import moe.pine.emotions.slack.models.Status;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import static moe.pine.emotions.slack.Slack.TIMEOUT;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("NullableProblems")
public class SlackTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    private Slack slack;

    @Before
    public void setUp() {
        when(restTemplateBuilder.setConnectTimeout(TIMEOUT)).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.setReadTimeout(TIMEOUT)).thenReturn(restTemplateBuilder);
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        slack = new Slack(restTemplateBuilder);
    }

    @Test
    public void constructorTest() {
        final Slack slack = new Slack(restTemplateBuilder);
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
