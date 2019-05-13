package moe.pine.emotions.log.utils;

import moe.pine.emotions.log.models.AvatarType;
import org.springframework.stereotype.Component;

@Component
public class AvatarLastUpdatedKeyBuilder {
    private static final String KEY_FORMAT = "avatar_last_updated_at:%s";

    public String buildKey(AvatarType avatarType) {
        return String.format(KEY_FORMAT, avatarType.getId());
    }
}
