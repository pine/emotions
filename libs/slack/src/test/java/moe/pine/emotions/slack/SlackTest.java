package moe.pine.emotions.slack;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.fileupload.FileItemHeaders;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.MultipartStream;
import org.apache.http.entity.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("ConstantConditions")
public class SlackTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private MockWebServer mockWebServer;
    private Slack slack;

    @BeforeEach
    public void setUp() {
        mockWebServer = new MockWebServer();
        slack = new Slack(
            WebClient.builder(),
            mockWebServer.url("").toString());
    }

    @AfterEach
    public void tearDown() throws Exception {
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
    public void setUserPhotoTest() throws Exception {
        final MockResponse mockResponse = new MockResponse();
        final Status status = Status.builder().ok(true).build();
        final String json = OBJECT_MAPPER.writeValueAsString(status);
        mockResponse.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        mockResponse.setBody(json);
        mockWebServer.enqueue(mockResponse);

        final byte[] image = {0x01, 0x02, 0x03};
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
            @Override
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
    public void setUserPhotoTest_emptyToken() {
        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                slack.setUserPhoto("", new byte[]{0x00});
            });

        assertEquals("`token` should not be empty.", exception.getMessage());
    }

    @Test
    public void setUserPhotoTest_nullToken() {
        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                slack.setUserPhoto(null, new byte[]{0x00});
            });

        assertEquals("`token` should not be empty.", exception.getMessage());
    }

    @Test
    @SuppressWarnings("ZeroLengthArrayAllocation")
    public void setUserPhotoTest_emptyImage() {
        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                slack.setUserPhoto("token", new byte[]{});
            });

        assertEquals("`image` should not be empty.", exception.getMessage());
    }

    @Test
    public void setUserPhotoTest_nullImage() {
        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                slack.setUserPhoto("token", null);
            });

        assertEquals("`image` should not be empty.", exception.getMessage());
    }
}
