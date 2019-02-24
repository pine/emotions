package moe.pine.emotions.gravatar;

public class GravatarException extends Exception {
    public GravatarException(Throwable cause) {
        super(cause);
    }

    public GravatarException(String message, Throwable cause) {
        super(message, cause);
    }

    public GravatarException(String message) {
        super(message);
    }
}
