package moe.pine.emotions.slack;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import moe.pine.emotions.slack.models.Status;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.concurrent.TimeUnit;

import static moe.pine.emotions.slack.Slack.USERS_SET_PHOTO_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SuppressWarnings("NullableProblems")
public class SlackTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private MockWebServer mockWebServer;
    private Slack slack;

    @Before
    public void setUp() {
        mockWebServer = new MockWebServer();
        slack = new Slack(
            WebClient.builder(),
            mockWebServer.url("").toString());
    }

    @After
    @SneakyThrows
    public void tearDown() {
        mockWebServer.shutdown();
    }

    @Test
    public void constructorTest() {
        final Slack slack = new Slack(WebClient.builder());
        final WebClient webClient = Whitebox.getInternalState(slack, "webClient");
        assertNotNull(webClient);
    }

    @Test
    @SneakyThrows
    public void setUserPhotoTest() {
        final MockResponse mockResponse = new MockResponse();
        final Status status = Status.builder().ok(true).build();
        final String body = OBJECT_MAPPER.writeValueAsString(status);
        mockResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        mockResponse.setBody(body);
        mockWebServer.enqueue(mockResponse);

        slack.setUserPhoto("token", new byte[]{0x01, 0x02, 0x03});

        assertEquals(1, mockWebServer.getRequestCount());

        final RecordedRequest recordedRequest = mockWebServer.takeRequest(10L, TimeUnit.SECONDS);
        assertNotNull(recordedRequest);
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(USERS_SET_PHOTO_PATH, recordedRequest.getPath());

        // TODO: parse body
    }

    @Test
    @SneakyThrows
    public void setUserPhotoEmptyTokenTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`token` should not be empty.");

        slack.setUserPhoto("", new byte[]{0x00});
    }

    @Test
    @SneakyThrows
    public void setUserPhotoEmptyImageTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        slack.setUserPhoto("token", new byte[]{});
    }

    @Test
    @SneakyThrows
    @SuppressWarnings("ConstantConditions")
    public void setUserPhotoTest_nullImage() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        slack.setUserPhoto("token", null);
    }
}
