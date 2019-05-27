package moe.pine.emotions.log.utils;

import moe.pine.emotions.log.models.AvatarType;

import javax.annotation.Nonnull;

public class AvatarUpdatedKeyBuilder {
    private static final String KEY_FORMAT = "avatar_updated:%s";

    public String buildKey(@Nonnull final AvatarType avatarType) {
        return String.format(KEY_FORMAT, avatarType.getId());
    }
}
