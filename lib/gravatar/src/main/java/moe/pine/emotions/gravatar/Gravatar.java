package moe.pine.emotions.gravatar;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClientException;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Getter
public class Gravatar {

    @NotNull
    private final GravatarClient gravatarClient;

    @NotNull
    private final String password;

    public Gravatar(
        @NotNull final GravatarClient gravatarClient,
        @NotNull final String password
    ) {
        checkNotNull(gravatarClient);
        checkArgument(StringUtils.isNotEmpty(password), "`password` should not be empty");

        this.gravatarClient = gravatarClient;
        this.password = password;
    }

    @NotNull
    public List<UserImage> getUserImages() {
        try {
            return gravatarClient.getUserImages(password);
        } catch (GravatarClientException e) {
            throw new GravatarException(e);
        }
    }

    public void chooseImage(
        @NotNull final List<String> images,
        @NotNull final List<String> addresses
    ) {
        checkArgument(CollectionUtils.isNotEmpty(images), "`images` should not be empty");
        checkArgument(CollectionUtils.isNotEmpty(addresses), "`addresses` should not be empty");

        final int chosen = new Random().nextInt(images.size());
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
