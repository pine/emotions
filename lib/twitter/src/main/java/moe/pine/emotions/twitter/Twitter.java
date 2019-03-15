package moe.pine.emotions.twitter;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import javax.annotation.Nonnull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
public class Twitter {
    private final twitter4j.Twitter underlying;

    public Twitter(
        @Nonnull final String consumerKey,
        @Nonnull final String consumerSecret,
        @Nonnull final String accessToken,
        @Nonnull final String accessTokenSecret
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
        @Nonnull final twitter4j.Twitter twitter
    ) {
        underlying = checkNotNull(twitter);
    }

    /**
     * @see <a href="http://twitter4j.org/javadoc/twitter4j/TwitterImpl.html">TwitterImpl</a>
     */
    public void updateProfileImage(@Nonnull final byte[] image) {
        checkArgument(ArrayUtils.isNotEmpty(image), "`image` should not be empty.");

        log.info("Updating profile image");

        try {
            final InputStream stream = new ByteArrayInputStream(image);
            underlying.updateProfileImage(stream);
        } catch (twitter4j.TwitterException e) {
            throw new TwitterException(e);
        }

        log.info("Updated profile image");
    }
}
