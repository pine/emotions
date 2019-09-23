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
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilderFactory;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static moe.pine.emotions.bookmeter.WebAgent.TIMEOUT;
import static moe.pine.emotions.bookmeter.WebAgent.USER_AGENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"NullableProblems", "ConstantConditions"})
public class WebAgentTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private WebClient.Builder webClientBuilder;

    private WebClient webClient;
    private WebAgent webAgent;
    private MockWebServer mockWebServer;

    @Before
    public void setUp() {
        mockWebServer = new MockWebServer();

        webClient =
            spy(WebClient.builder()
                .baseUrl(mockWebServer.url("").toString())
                .build());
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        webAgent = new WebAgent(webClientBuilder);
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
        assertEquals(USER_AGENT, recordedRequest.getHeader(HttpHeaders.USER_AGENT));
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
        assertEquals(USER_AGENT, recordedRequest.getHeader(HttpHeaders.USER_AGENT));
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

    @Test
    public void getAccountTest_emptyBody() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("An empty body received");

        final MockResponse mockResponse = new MockResponse().setBody("");
        mockWebServer.enqueue(mockResponse);

        final MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();
        webAgent.getAccount(cookies);
    }

    @Test
    @SuppressWarnings({"unchecked", "UnassignedFluxMonoInstance"})
    public void postTest() {
        final String path = "/foobar";
        final MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.set("foo", "1");

        final MultiValueMap<String, String> cookies = new LinkedMultiValueMap<>();
        cookies.set("bar", "2");

        final WebClient.RequestBodyUriSpec requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);
        final WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        final Mono<ClientResponse> clientResponseMono = mock(Mono.class);
        final ClientResponse clientResponse = mock(ClientResponse.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(path)).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.header(HttpHeaders.USER_AGENT, USER_AGENT)).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.syncBody(formData)).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.cookies(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.exchange()).thenReturn(clientResponseMono);
        when(clientResponseMono.block(TIMEOUT)).thenReturn(clientResponse);

        final ClientResponse actual = webAgent.post(path, formData, cookies);
        assertSame(clientResponse, actual);

        final ArgumentCaptor<Consumer<MultiValueMap<String, String>>> consumerCaptor =
            ArgumentCaptor.forClass(Consumer.class);

        verify(webClient).post();
        verify(requestBodyUriSpec).uri(path);
        verify(requestBodyUriSpec).header(HttpHeaders.USER_AGENT, USER_AGENT);
        verify(requestBodyUriSpec).syncBody(formData);
        verify(requestHeadersSpec).cookies(consumerCaptor.capture());
        verify(requestHeadersSpec).exchange();
        verify(clientResponseMono).block(TIMEOUT);

        final Consumer<MultiValueMap<String, String>> consumer = consumerCaptor.getValue();
        final MultiValueMap<String, String> builder = new LinkedMultiValueMap<>();
        consumer.accept(builder);

        assertEquals(cookies, builder);
    }
}
