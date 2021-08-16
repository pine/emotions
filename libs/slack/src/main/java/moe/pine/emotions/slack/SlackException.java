package moe.pine.emotions.slack;

public class SlackException extends RuntimeException {
    private static final long serialVersionUID = -6861269934478822067L;

    SlackException(String message) {
        super(message);
    }
}
