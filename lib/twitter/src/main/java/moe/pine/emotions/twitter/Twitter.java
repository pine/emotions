package moe.pine.emotions.twitter;

import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import java.io.ByteArrayInputStream;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class Twitter {
    private final twitter4j.Twitter underlying;

    public Twitter(
        final String consumerKey,
        final String consumerSecret,
        final String accessToken,
        final String accessTokenSecret
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

        final var twitterFactory = new TwitterFactory(conf);
        underlying = twitterFactory.getInstance();
    }

    @VisibleForTesting
    Twitter(
        final twitter4j.Twitter twitter
    ) {
        underlying = Objects.requireNonNull(twitter);
    }

    /**
     * @see <a href="http://twitter4j.org/javadoc/twitter4j/TwitterImpl.html">TwitterImpl</a>
     */
    public void updateProfileImage(final byte[] image) {
        checkArgument(ArrayUtils.isNotEmpty(image), "`image` should not be empty.");

        log.info("Updating profile image");

        final var stream = new ByteArrayInputStream(image);
        try {
            underlying.updateProfileImage(stream);
        } catch (twitter4j.TwitterException e) {
            throw new TwitterException(e);
        }

        log.info("Updated profile image");
    }
}
