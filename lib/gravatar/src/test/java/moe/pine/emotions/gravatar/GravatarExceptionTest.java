package moe.pine.emotions.gravatar;

import org.junit.Assert;
import org.junit.Test;

public class GravatarExceptionTest {
    @Test
    public void constructorByMessageTest() {
        final Exception exception = new GravatarException("Hello");
        Assert.assertEquals("Hello", exception.getMessage());
    }

    @Test
    public void constructorByThrowableTest() {
        final Throwable throwable = new Exception();
        final Exception exception = new GravatarException(throwable);
        Assert.assertSame(throwable, exception.getCause());
    }
}
