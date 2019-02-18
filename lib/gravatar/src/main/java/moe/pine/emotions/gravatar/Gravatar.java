package moe.pine.emotions.gravatar;

import lombok.Builder;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClientException;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;

import javax.validation.constraints.NotNull;

@Slf4j
@ToString
public class Gravatar {
    @NotNull
    private final String email;

    @NotNull
    @ToString.Exclude
    private final String password;

    @NotNull
    @ToString.Exclude
    private final GravatarClient gravatarClient;

    @Builder
    public Gravatar(String email, String password) {
        this.email = email;
        this.password = password;
        this.gravatarClient = new GravatarClient(email);
    }

    public UserImage[] getUserImages() throws GravatarClientException {
        return gravatarClient.getUserImages(password);
    }

    public void choiceImage() {
    }
}
