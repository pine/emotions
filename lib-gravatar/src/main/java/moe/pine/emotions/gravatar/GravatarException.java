package moe.pine.emotions.gravatar;

public class GravatarException extends RuntimeException {
    GravatarException(Throwable cause) {
        super(cause);
    }

    GravatarException(String message) {
        super(message);
    }
}
