package moe.pine.emotions.gravatar;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class GravatarExceptionTest {
    @Test
    public void constructorByMessageTest() {
        final Exception exception = new GravatarException("Hello");
        assertEquals("Hello", exception.getMessage());
    }

    @Test
    public void constructorByThrowableTest() {
        final Throwable throwable = new Exception();
        final Exception exception = new GravatarException(throwable);
        assertSame(throwable, exception.getCause());
    }
}
