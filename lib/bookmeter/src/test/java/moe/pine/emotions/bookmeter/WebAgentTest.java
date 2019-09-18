package moe.pine.emotions.bookmeter;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilderFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"NullableProblems", "ConstantConditions"})
public class WebAgentTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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
        final MockResponse mockResponse = new MockResponse()
            .setBody("body")
            .setHeader(HttpHeaders.SET_COOKIE, "name=value");
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

    @Test
    public void getLoginTest_emptyBody() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("An empty body received.");

        final MockResponse mockResponse = new MockResponse()
            .setBody(StringUtils.EMPTY);
        mockWebServer.enqueue(mockResponse);

        webAgent.getLogin();
    }

    @Test
    public void getLoginTest_notFound() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Illegal status code :: statusCode=404");

        final MockResponse mockResponse = new MockResponse()
            .setResponseCode(HttpStatus.NOT_FOUND.value())
            .setBody("body");
        mockWebServer.enqueue(mockResponse);

        webAgent.getLogin();
    }

    @Test
    public void postLoginTest() throws InterruptedException {
        final MockResponse mockResponse = new MockResponse()
            .setResponseCode(HttpStatus.FOUND.value())
            .addHeader(HttpHeaders.LOCATION, WebAgent.BASE_URL)
            .addHeader(HttpHeaders.SET_COOKIE, "response-cookie=qwerty");
        mockWebServer.enqueue(mockResponse);

        final MultiValueMap<String, String> formData =
            new LinkedMultiValueMap<>() {{
                set("form-data", "abc");
            }};
        final MultiValueMap<String, String> requestCookies =
            new LinkedMultiValueMap<>() {{
                set("request-cookie", "12345");
            }};
        final MultiValueMap<String, String> responseCookies =
            new LinkedMultiValueMap<>() {{
                set("response-cookie", "qwerty");
            }};

        final WebAgent.PostLoginResponse expected =
            WebAgent.PostLoginResponse.builder()
                .cookies(responseCookies)
                .build();

        final WebAgent.PostLoginResponse actual = webAgent.postLogin(formData, requestCookies);
        assertEquals(expected, actual);

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals(1, mockWebServer.getRequestCount());
        assertEquals(HttpMethod.POST.name(), recordedRequest.getMethod());
        assertEquals(WebAgent.LOGIN_PATH, recordedRequest.getPath());
        assertEquals(WebAgent.USER_AGENT, recordedRequest.getHeader(HttpHeaders.USER_AGENT));
        assertEquals("request-cookie=12345", recordedRequest.getHeader(HttpHeaders.COOKIE));
        assertEquals("form-data=abc", recordedRequest.getBody().readUtf8());
    }

    @Test
    public void postLoginTest_illegalStatus() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("Illegal status code received. :: statusCode=200");

        final MockResponse mockResponse = new MockResponse();
        mockWebServer.enqueue(mockResponse);

        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        final MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();

        webAgent.postLogin(formData, cookies);
    }

    @Test
    public void postLoginTest_emptyLocationHeader() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("An empty `Location` header received.");

        final MockResponse mockResponse = new MockResponse()
            .setResponseCode(HttpStatus.FOUND.value());
        mockWebServer.enqueue(mockResponse);

        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        final MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();

        webAgent.postLogin(formData, cookies);
    }

    @Test
    public void getAccountTest() throws InterruptedException {
        final MockResponse mockResponse = new MockResponse()
            .setBody("body")
            .setHeader(HttpHeaders.SET_COOKIE, "res=12345");
        mockWebServer.enqueue(mockResponse);

        final MultiValueMap<String, String> requestCookies = new LinkedMultiValueMap<>();
        requestCookies.set("req", "abc");

        final MultiValueMap<String, String> responseCookies = new LinkedMultiValueMap<>();
        responseCookies.set("res", "12345");

        final WebAgent.GetAccountResponse expected =
            WebAgent.GetAccountResponse.builder()
                .body("body")
                .cookies(responseCookies)
                .build();
        final WebAgent.GetAccountResponse actual = webAgent.getAccount(requestCookies);
        assertEquals(expected, actual);

        final RecordedRequest recordedRequest = mockWebServer.takeRequest();
        assertEquals(1, mockWebServer.getRequestCount());
        assertEquals(HttpMethod.GET.name(), recordedRequest.getMethod());
        assertEquals(WebAgent.ACCOUNT_PATH, recordedRequest.getPath());
        assertEquals("req=abc", recordedRequest.getHeader(HttpHeaders.COOKIE));
    }
}
