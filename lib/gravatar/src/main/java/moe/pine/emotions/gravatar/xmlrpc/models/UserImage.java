package moe.pine.emotions.gravatar.xmlrpc.models;

import lombok.Builder;
import lombok.Value;

import javax.annotation.Nonnull;

@Value
@Builder
public class UserImage {
    @Nonnull
    private final String hash;

    @Nonnull
    private final String url;
}
