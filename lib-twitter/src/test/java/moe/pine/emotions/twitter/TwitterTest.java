package moe.pine.emotions.twitter;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;

import java.io.InputStream;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({
    "ConstantConditions",
    "UnnecessaryFullyQualifiedName",
    "ZeroLengthArrayAllocation"
})
class TwitterTest {
    @Mock
    private twitter4j.Twitter underlying;

    @Mock
    private twitter4j.User user;

    @Nested
    class New {
        @Test
        void success() throws Exception {
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
        void emptyConsumerKey() {
            assertThatThrownBy(() ->
                new Twitter(
                    "",
                    "consumer_secret",
                    "access_token",
                    "access_token_secret",
                    Duration.ofSeconds(60),
                    Duration.ofSeconds(120)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("consumerKey");
        }

        @Test
        void emptyConsumerSecret() {
            assertThatThrownBy(() ->
                new Twitter(
                    "consumer_key",
                    "",
                    "access_token",
                    "access_token_secret",
                    Duration.ofSeconds(60),
                    Duration.ofSeconds(120)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("consumerSecret");
        }

        @Test
        void emptyAccessToken() {
            assertThatThrownBy(() ->
                new Twitter(
                    "consumer_key",
                    "consumer_secret",
                    "",
                    "access_token_secret",
                    Duration.ofSeconds(60),
                    Duration.ofSeconds(120)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("accessToken");
        }

        @Test
        void emptyAccessTokenSecret() {
            assertThatThrownBy(() ->
                new Twitter(
                    "consumer_key",
                    "consumer_secret",
                    "access_token",
                    "",
                    Duration.ofSeconds(60),
                    Duration.ofSeconds(120)))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("accessTokenSecret");
        }
    }

    @Nested
    class UpdateProfileImage {
        @Test
        void success() throws Exception {
            final Twitter twitter = new Twitter(underlying);

            when(underlying.updateProfileImage(any(InputStream.class))).thenReturn(user);

            twitter.updateProfileImage(new byte[]{0x01, 0x02, 0x03});
            verify(underlying).updateProfileImage(any(InputStream.class));
        }

        @Test
        void emptyImage() {
            final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    final Twitter twitter = new Twitter(underlying);
                    twitter.updateProfileImage(new byte[]{});
                });

            assertEquals("`image` should not be empty.", exception.getMessage());
        }

        @Test
        void nullImage() {
            final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> {
                    final Twitter twitter = new Twitter(underlying);

                    twitter.updateProfileImage(null);
                });

            assertEquals("`image` should not be empty.", exception.getMessage());
        }
    }
}
