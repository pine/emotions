package moe.pine.emotions.slack;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SlackExceptionTest {
    @Test
    public void constructorTest() {
        final Exception exception = new SlackException("message");
        assertEquals("message", exception.getMessage());
    }
}
