package moe.pine.emotions.twitter;

import org.junit.Test;

import static org.junit.Assert.assertSame;

public class TwitterExceptionTest {
    @Test
    public void constructorTest() {
        final Throwable throwable = new Exception();
        final Exception exception = new TwitterException(throwable);

        assertSame(throwable, exception.getCause());
    }
}
