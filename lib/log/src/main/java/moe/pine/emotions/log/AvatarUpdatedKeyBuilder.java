package moe.pine.emotions.log;

public class AvatarUpdatedKeyBuilder {
    private static final String KEY_FORMAT = "avatar_updated:%s";

    public String buildKey(final AvatarType avatarType) {
        return String.format(KEY_FORMAT, avatarType.getId());
    }
}
