package moe.pine.emotions.log.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AvatarType {
    GRAVATAR("gravatar"),
    SLACK("slack"),
    TWITTER("twitter"),
    ;

    private final String id;
}