package moe.pine.emotions.cloudstorage;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class CloudStorageExceptionTest {
    @Test
    public void constructorMessageTest() {
        final Exception exception = new CloudStorageException("message");
        assertEquals("message", exception.getMessage());
    }

    @Test
    public void constructorThrowableTest() {
        final Throwable throwable = new Exception();
        final Exception exception = new CloudStorageException(throwable);
        assertSame(throwable, exception.getCause());
    }
}
