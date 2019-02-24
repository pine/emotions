package moe.pine.emotions.gravatar;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClientException;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;

import javax.annotation.Nonnull;

@Slf4j
@ToString
public class Gravatar {
    @Nonnull
    private final String email;

    @Nonnull
    @ToString.Exclude
    private final String password;

    @Nonnull
    @ToString.Exclude
    private final GravatarClient gravatarClient;

    public Gravatar(@Nonnull final String email, @Nonnull final String password) {
        this.email = email;
        this.password = password;
        this.gravatarClient = new GravatarClient(email);
    }

    @Nonnull
    public UserImage[] getUserImages() throws GravatarClientException {
        return gravatarClient.getUserImages(password);
    }

    public void choiceImage(String images) {
    }
}
