package moe.pine.emotions.gravatar;

public class GravatarException extends Exception {
    GravatarException(Throwable cause) {
        super(cause);
    }

    GravatarException(String message) {
        super(message);
    }
}
