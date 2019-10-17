package moe.pine.emotions.slack;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import moe.pine.emotions.slack.models.Status;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.http.entity.ContentType;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static moe.pine.emotions.slack.Slack.BASE_URL;
import static moe.pine.emotions.slack.Slack.USERS_SET_PHOTO_PATH;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"NullableProblems", "ConstantConditions"})
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
        final WebClient.Builder webClientBuilder = mock(WebClient.Builder.class);
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(WebClient.create());

        final Slack slack = new Slack(webClientBuilder);
        final WebClient webClient = Whitebox.getInternalState(slack, "webClient");
        assertNotNull(webClient);

        verify(webClientBuilder).baseUrl(BASE_URL);
        verify(webClientBuilder).build();
    }

    @Test
    @SneakyThrows
    public void setUserPhotoTest() {
        final MockResponse mockResponse = new MockResponse();
        final Status status = Status.builder().ok(true).build();
        final String json = OBJECT_MAPPER.writeValueAsString(status);
        mockResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        mockResponse.setBody(json);
        mockWebServer.enqueue(mockResponse);

        final byte[] image = new byte[]{0x01, 0x02, 0x03};
        slack.setUserPhoto("token", image);

        assertEquals(1, mockWebServer.getRequestCount());

        final RecordedRequest recordedRequest = mockWebServer.takeRequest(10L, TimeUnit.SECONDS);
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(USERS_SET_PHOTO_PATH, recordedRequest.getPath());

        final ContentType contentType =
            ContentType.parse(recordedRequest.getHeader(HttpHeaders.CONTENT_TYPE));
        final String boundary = contentType.getParameter("boundary");
        assertNotNull(boundary);

        final String body = recordedRequest.getBody().readUtf8();
        System.out.println(body);

        final var multipartStream =
            new MultipartStream(
                new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)),
                boundary.getBytes(StandardCharsets.UTF_8), 4096, null);

        final String headerText = multipartStream.readHeaders();
        final var fileUpload = new FileUpload() {
            public FileItemHeaders getParsedHeaders(String headerPart) {
                return super.getParsedHeaders(headerPart);
            }
        };

        final var headers = fileUpload.getParsedHeaders(headerText);
        assertEquals("form-data; name=\"image\"; filename=\"image.png\"",
            headers.getHeader("Content-Disposition"));

        final var outputStream = new ByteArrayOutputStream();
        multipartStream.readBodyData(outputStream);
        assertArrayEquals(image, outputStream.toByteArray());
    }

    @Test
    @SneakyThrows
    public void setUserPhotoTest_emptyToken() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`token` should not be empty.");

        slack.setUserPhoto("", new byte[]{0x00});
    }

    @Test
    @SneakyThrows
    public void setUserPhotoTest_nullToken() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`token` should not be empty.");

        slack.setUserPhoto(null, new byte[]{0x00});
    }

    @Test
    @SneakyThrows
    public void setUserPhotoTest_emptyImage() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        slack.setUserPhoto("token", new byte[]{});
    }

    @Test
    @SneakyThrows
    public void setUserPhotoTest_nullImage() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        slack.setUserPhoto("token", null);
    }
}
