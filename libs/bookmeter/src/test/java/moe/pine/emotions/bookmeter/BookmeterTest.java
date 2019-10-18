package moe.pine.emotions.bookmeter;

import lombok.SneakyThrows;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("NullableProblems")
public class BookmeterTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Fetcher fetcher;

    @Mock
    private Parser parser;

    @Mock
    private FormDataBuilder formDataBuilder;

    @InjectMocks
    private Bookmeter bookmeter;

    @Test
    @SneakyThrows
    public void updateProfileImageTest() {
        final byte[] image = {0x01, 0x02, 0x03};

        final MultiValueMap<String, String> cookies1 = new LinkedMultiValueMap<>();
        cookies1.add("name", "cookies1");

        final MultiValueMap<String, String> cookies2 = new LinkedMultiValueMap<>();
        cookies2.add("name", "cookies2");

        final MultiValueMap<String, String> cookies3 = new LinkedMultiValueMap<>();
        cookies2.add("name", "cookies3");

        final MultiValueMap<String, String> loginFormData = new LinkedMultiValueMap<>();
        loginFormData.add("name", "loginFormData");

        final MultiValueMap<String, HttpEntity<?>> accountFormData = new LinkedMultiValueMap<>();
        accountFormData.add("name", new HttpEntity<>("accountFormData"));

        final Fetcher.GetLoginResponse getLoginResponse =
            Fetcher.GetLoginResponse.builder()
                .body("body1")
                .cookies(cookies1)
                .build();

        final Fetcher.PostLoginResponse postLoginResponse =
            Fetcher.PostLoginResponse.builder()
                .cookies(cookies2)
                .build();

        final Fetcher.GetAccountResponse getAccountResponse =
            Fetcher.GetAccountResponse.builder()
                .cookies(cookies3)
                .body("body2")
                .build();

        final Parser.AccountFormData parserAccountFormData =
            Parser.AccountFormData.builder()
                .authenticityToken("authenticityToken2")
                .name("name")
                .build();

        when(fetcher.getLogin()).thenReturn(getLoginResponse);
        when(fetcher.getAccount(cookies2)).thenReturn(getAccountResponse);
        when(fetcher.postLogin(loginFormData, cookies1)).thenReturn(postLoginResponse);
        doNothing().when(fetcher).postAccount(accountFormData, cookies3);
        when(formDataBuilder.buildLoginFormData("authenticityToken1")).thenReturn(loginFormData);
        when(formDataBuilder.buildAccountFormData("authenticityToken2", "name", image))
            .thenReturn(accountFormData);
        when(parser.parseLoginForm("body1")).thenReturn("authenticityToken1");
        when(parser.parseAccountForm("body2")).thenReturn(parserAccountFormData);

        bookmeter.updateProfileImage(image);

        verify(fetcher).getLogin();
        verify(fetcher).getAccount(cookies2);
        verify(fetcher).postLogin(loginFormData, cookies1);
        verify(fetcher).postAccount(accountFormData, cookies3);
        verify(formDataBuilder).buildLoginFormData("authenticityToken1");
        verify(formDataBuilder).buildAccountFormData("authenticityToken2", "name", image);
        verify(parser).parseLoginForm("body1");
        verify(parser).parseAccountForm("body2");
    }

    @Test
    @SneakyThrows
    public void updateProfileImageTest_emptyAuthenticityToken() {
        expectedException.expect(RuntimeException.class);
        expectedException.expectMessage("`authenticityToken` is not found.");

        final byte[] image = {0x01, 0x02, 0x03};
        final Fetcher.GetLoginResponse getLoginResponse =
            Fetcher.GetLoginResponse.builder().body("body").build();

        when(fetcher.getLogin()).thenReturn(getLoginResponse);
        when(parser.parseLoginForm("body")).thenReturn("");

        bookmeter.updateProfileImage(image);
    }
}
