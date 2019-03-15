package moe.pine.emotions.twitter;

import lombok.SneakyThrows;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.reflect.Whitebox;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TwitterTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private twitter4j.Twitter underlying;

    @Mock
    private twitter4j.User user;

    @Test
    @SneakyThrows
    public void constructorTest() {
        final Twitter twitter = new Twitter(
            "consumer_key",
            "consumer_secret",
            "access_token",
            "access_token_secret"
        );

        final twitter4j.Twitter underlying =
            Whitebox.getInternalState(twitter, "underlying");
        final Configuration configuration = underlying.getConfiguration();
        final AccessToken accessToken = underlying.getOAuthAccessToken();

        assertEquals("consumer_key", configuration.getOAuthConsumerKey());
        assertEquals("consumer_secret", configuration.getOAuthConsumerSecret());
        assertEquals("access_token", accessToken.getToken());
        assertEquals("access_token_secret", accessToken.getTokenSecret());
    }

    @Test
    public void constructorEmptyConsumerKeyTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`consumerKey` should not be empty.");

        new Twitter(
            "",
            "consumer_secret",
            "access_token",
            "access_token_secret"
        );
    }

    @Test
    public void constructorEmptyConsumerSecretTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`consumerSecret` should not be empty.");

        new Twitter(
            "consumer_key",
            "",
            "access_token",
            "access_token_secret"
        );
    }

    @Test
    public void constructorEmptyAccessTokenTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`accessToken` should not be empty.");

        new Twitter(
            "consumer_key",
            "consumer_secret",
            "",
            "access_token_secret"
        );
    }

    @Test
    public void constructorEmptyAccessTokenSecretTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`accessTokenSecret` should not be empty.");

        new Twitter(
            "consumer_key",
            "consumer_secret",
            "access_token",
            ""
        );
    }

    @Test
    @SneakyThrows
    public void updateProfileImageTest() {
        final Twitter twitter = new Twitter(underlying);

        when(underlying.updateProfileImage((InputStream) any())).thenReturn(user);

        twitter.updateProfileImage(new byte[]{0x01, 0x02, 0x03});
        verify(underlying, times(1)).updateProfileImage((InputStream) any());
    }

    @Test
    public void updateProfileImageEmptyImageTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`image` should not be empty.");

        final Twitter twitter = new Twitter(underlying);
        twitter.updateProfileImage(new byte[]{});
    }
}
