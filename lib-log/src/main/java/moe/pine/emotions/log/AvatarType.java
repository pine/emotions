package moe.pine.emotions.log;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AvatarType {
    GRAVATAR("gravatar"),
    SLACK("slack"),
    TWITTER("twitter"),
    BOOKMETER("bookmeter"),
    ;

    private final String id;
}
