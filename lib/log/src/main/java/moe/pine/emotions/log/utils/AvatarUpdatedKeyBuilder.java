package moe.pine.emotions.log.utils;

import moe.pine.emotions.log.models.AvatarType;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

@Component
public class AvatarUpdatedKeyBuilder {
    private static final String KEY_FORMAT = "avatar_updated:%s";

    public String buildKey(@Nonnull final AvatarType avatarType) {
        return String.format(KEY_FORMAT, avatarType.getId());
    }
}
