package moe.pine.emotions.gravatar;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClientException;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Getter(AccessLevel.PACKAGE)
public class Gravatar {
    private final Random random;
    private final GravatarClient gravatarClient;
    private final String password;

    public Gravatar(
        final GravatarClient gravatarClient,
        final String password
    ) {
        this(gravatarClient, password, new Random());
    }

    protected Gravatar(
        final GravatarClient gravatarClient,
        final String password,
        final Random random
    ) {
        checkArgument(StringUtils.isNotEmpty(password), "`password` should not be empty");

        this.random = Objects.requireNonNull(random);
        this.gravatarClient = Objects.requireNonNull(gravatarClient);
        this.password = password;
    }

    public List<UserImage> getUserImages() {
        try {
            return gravatarClient.getUserImages(password);
        } catch (GravatarClientException e) {
            throw new GravatarException(e);
        }
    }

    public void chooseImage(
        final List<String> images,
        final List<String> addresses
    ) {
        checkArgument(CollectionUtils.isNotEmpty(images), "`images` should not be empty");
        checkArgument(CollectionUtils.isNotEmpty(addresses), "`addresses` should not be empty");

        final int chosen = random.nextInt(images.size());
        final String chosenImage = images.get(chosen);

        final Map<String, Boolean> statuses;
        try {
            statuses = gravatarClient.useUserImage(password, chosenImage, addresses);
        } catch (GravatarClientException e) {
            throw new GravatarException(e);
        }

        final long successfulNumber = statuses.values()
            .stream()
            .filter(status -> status)
            .count();
        if (addresses.size() != successfulNumber) {
            throw new GravatarException(String.format(
                "Failed to use a chosen image :: chosen-image=%s, response=%s",
                chosenImage, statuses.toString()));
        }
    }
}
