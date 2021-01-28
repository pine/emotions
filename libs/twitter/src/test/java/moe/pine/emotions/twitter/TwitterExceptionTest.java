package moe.pine.emotions.twitter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;

public class TwitterExceptionTest {
    @Test
    public void constructorTest() {
        final Throwable throwable = new Exception();
        final Exception exception = new TwitterException(throwable);

        assertSame(throwable, exception.getCause());
    }
}
