package moe.pine.emotions.slack;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SlackExceptionTest {
    @Test
    public void constructorTest() {
        final Exception exception = new SlackException("message");
        assertEquals("message", exception.getMessage());
    }
}
