package moe.pine.emotions.bookmeter;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilderFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"NullableProblems", "ConstantConditions"})
public class WebAgentTest {
    private WebAgent webAgent;
    private MockWebServer mockWebServer;

    @Before
    public void setUp() {
        mockWebServer = new MockWebServer();
        webAgent = new WebAgent(WebClient.builder(), mockWebServer.url("").toString());
    }

    @After
    public void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void constructorTest() {
        final WebAgent webAgent = new WebAgent(WebClient.builder());
        final WebClient webClient = Whitebox.getInternalState(webAgent, "webClient");
        final UriBuilderFactory uriBuilderFactory =
            Whitebox.getInternalState(webClient, "uriBuilderFactory");

        final String actual = uriBuilderFactory.uriString("").build().toString();
        assertEquals(WebAgent.BASE_URL, actual);
    }

    @Test
    public void getLoginTest() throws InterruptedException {
        final MockResponse mockResponse = new MockResponse();
        mockResponse.setBody("body");
        mockResponse.setHeader(HttpHeaders.SET_COOKIE, "name=value");
        mockWebServer.enqueue(mockResponse);

        final MultiValueMap<String, String> expectedCookies = new LinkedMultiValueMap<>();
        expectedCookies.set("name", "value");

        final WebAgent.GetLoginResponse getLoginResponse = webAgent.getLogin();
        assertEquals("body", getLoginResponse.getBody());
        assertEquals(expectedCookies, getLoginResponse.getCookies());

        final RecordedRequest recordedRequest =
            mockWebServer.takeRequest(1L, TimeUnit.SECONDS);
        assertEquals(1, mockWebServer.getRequestCount());
        assertEquals(HttpMethod.GET.name(), recordedRequest.getMethod());
        assertEquals(WebAgent.LOGIN_PATH, recordedRequest.getPath());
        assertEquals(WebAgent.USER_AGENT, recordedRequest.getHeader(HttpHeaders.USER_AGENT));
    }
}
