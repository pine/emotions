package moe.pine.emotions.gravatar.xmlrpc.models;

import lombok.Builder;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
@Builder
public class UserImage {
    @NotNull
    private final String hash;

    @NotNull
    private final String url;
}
