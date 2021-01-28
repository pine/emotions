package moe.pine.emotions.twitter;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;

import java.io.InputStream;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({
    "CodeBlock2Expr",
    "ConstantConditions",
    "UnnecessaryFullyQualifiedName",
})
public class TwitterTest {
    public static final byte[] EMPTY_BYTES = {};

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
            "access_token_secret",
            Duration.ofSeconds(60),
            Duration.ofSeconds(120)
        );

        final twitter4j.Twitter underlying =
            (twitter4j.Twitter) ReflectionTestUtils.getField(twitter, "underlying");
        assertNotNull(underlying);

        final Configuration configuration = underlying.getConfiguration();
        final AccessToken accessToken = underlying.getOAuthAccessToken();

        assertEquals("consumer_key", configuration.getOAuthConsumerKey());
        assertEquals("consumer_secret", configuration.getOAuthConsumerSecret());
        assertEquals("access_token", accessToken.getToken());
        assertEquals("access_token_secret", accessToken.getTokenSecret());
    }

    @Test
    public void constructorEmptyConsumerKeyTest() {
        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                new Twitter(
                    "",
                    "consumer_secret",
                    "access_token",
                    "access_token_secret",
                    Duration.ofSeconds(60),
                    Duration.ofSeconds(120)
                );
            });

        assertEquals("`consumerKey` should not be empty.", exception.getMessage());
    }

    @Test
    public void constructorEmptyConsumerSecretTest() {
        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                new Twitter(
                    "consumer_key",
                    "",
                    "access_token",
                    "access_token_secret",
                    Duration.ofSeconds(60),
                    Duration.ofSeconds(120)
                );
            });

        assertEquals("`consumerSecret` should not be empty.", exception.getMessage());
    }

    @Test
    public void constructorEmptyAccessTokenTest() {
        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                new Twitter(
                    "consumer_key",
                    "consumer_secret",
                    "",
                    "access_token_secret",
                    Duration.ofSeconds(60),
                    Duration.ofSeconds(120)
                );
            });

        assertEquals("`accessToken` should not be empty.", exception.getMessage());
    }

    @Test
    public void constructorEmptyAccessTokenSecretTest() {
        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                new Twitter(
                    "consumer_key",
                    "consumer_secret",
                    "access_token",
                    "",
                    Duration.ofSeconds(60),
                    Duration.ofSeconds(120)
                );
            });

        assertEquals("`accessTokenSecret` should not be empty.", exception.getMessage());
    }

    @Test
    @SneakyThrows
    public void updateProfileImageTest() {
        final Twitter twitter = new Twitter(underlying);

        when(underlying.updateProfileImage(any(InputStream.class))).thenReturn(user);

        twitter.updateProfileImage(new byte[]{0x01, 0x02, 0x03});
        verify(underlying).updateProfileImage(any(InputStream.class));
    }

    @Test
    public void updateProfileImageEmptyImageTest() {
        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                final Twitter twitter = new Twitter(underlying);
                twitter.updateProfileImage(EMPTY_BYTES);
            });

        assertEquals("`image` should not be empty.", exception.getMessage());
    }

    @Test
    public void updateProfileImageNullImageTest() {
        final IllegalArgumentException exception =
            assertThrows(IllegalArgumentException.class, () -> {
                final Twitter twitter = new Twitter(underlying);

                twitter.updateProfileImage(null);
            });

        assertEquals("`image` should not be empty.", exception.getMessage());
    }
}
