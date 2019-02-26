package moe.pine.emotions.gravatar;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClientException;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import org.apache.commons.collections4.CollectionUtils;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@RequiredArgsConstructor
@Getter
public class Gravatar {

    @Nonnull
    private final GravatarClient gravatarClient;

    @Nonnull
    private final String password;

    @Nonnull
    public UserImage[] getUserImages() throws GravatarException {
        try {
            return gravatarClient.getUserImages(password);
        } catch (GravatarClientException e) {
            throw new GravatarException(e);
        }
    }

    public void choiceImage(
        @Nonnull final List<String> images,
        @Nonnull final List<String> addresses
    ) {
        checkArgument(CollectionUtils.isNotEmpty(images), "`images` cannot be empty");

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
