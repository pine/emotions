package moe.pine.emotions.slack;

import com.google.common.annotations.VisibleForTesting;

public class SlackException extends RuntimeException {
    @VisibleForTesting
    public SlackException(String message) {
        super(message);
    }
}
