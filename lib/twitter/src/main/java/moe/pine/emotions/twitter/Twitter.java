package moe.pine.emotions.twitter;

import com.google.common.annotations.VisibleForTesting;
import org.jetbrains.annotations.NotNull;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Twitter {
    private final twitter4j.Twitter underlying;

    public Twitter(
        @NotNull final String consumerKey,
        @NotNull final String consumerSecret,
        @NotNull final String accessToken,
        @NotNull final String accessTokenSecret
    ) {
        TwitterFactory twitterFactory = new TwitterFactory();
        underlying = twitterFactory.getInstance(new AccessToken(accessToken, accessTokenSecret));
        underlying.setOAuthConsumer(consumerKey, consumerSecret);
    }

    @VisibleForTesting
    Twitter(
        @NotNull final twitter4j.Twitter twitter
    ) {
        underlying = twitter;
    }

    /**
     * @see <a href="http://twitter4j.org/javadoc/twitter4j/TwitterImpl.html">TwitterImpl</a>
     */
    public void updateProfileImage(@NotNull final byte[] image) {
        try {
            final InputStream stream = new ByteArrayInputStream(image);
            underlying.updateProfileImage(stream);
        } catch (twitter4j.TwitterException e) {
            throw new TwitterException(e);
        }
    }
}
