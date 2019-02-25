package moe.pine.emotions.gravatar.xmlrpc;

import com.google.common.annotations.VisibleForTesting;

public class GravatarClientException extends Exception {
    @VisibleForTesting
    public GravatarClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
