package moe.pine.emotions.twitter;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class Twitter {
    private final twitter4j.Twitter underlying;

    public Twitter(
        @NotNull final String consumerKey,
        @NotNull final String consumerSecret,
        @NotNull final String accessToken,
        @NotNull final String accessTokenSecret
    ) {
        checkArgument(StringUtils.isNotEmpty(consumerKey), "`consumerKey` should not be empty.");
        checkArgument(StringUtils.isNotEmpty(consumerSecret), "`consumerSecret` should not be empty.");
        checkArgument(StringUtils.isNotEmpty(accessToken), "`accessToken` should not be empty.");
        checkArgument(StringUtils.isNotEmpty(accessTokenSecret), "`accessTokenSecret` should not be empty.");

        final Configuration conf = new ConfigurationBuilder()
            .setOAuthConsumerKey(consumerKey)
            .setOAuthConsumerSecret(consumerSecret)
            .setOAuthAccessToken(accessToken)
            .setOAuthAccessTokenSecret(accessTokenSecret)
            .build();

        final TwitterFactory twitterFactory = new TwitterFactory(conf);
        underlying = twitterFactory.getInstance();
    }

    @VisibleForTesting
    Twitter(
        @NotNull final twitter4j.Twitter twitter
    ) {
        underlying = checkNotNull(twitter);
    }

    /**
     * @see <a href="http://twitter4j.org/javadoc/twitter4j/TwitterImpl.html">TwitterImpl</a>
     */
    public void updateProfileImage(@NotNull final byte[] image) {
        checkArgument(ArrayUtils.isNotEmpty(image), "`image` should not be empty.");

        try {
            final InputStream stream = new ByteArrayInputStream(image);
            underlying.updateProfileImage(stream);
        } catch (twitter4j.TwitterException e) {
            throw new TwitterException(e);
        }
    }
}
