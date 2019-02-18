package moe.pine.emotions.gravatar.xmlrpc.models;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UserImage {
    private final String hash;
    private final String url;
}
