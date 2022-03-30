package moe.pine.emotions.cloudstorage;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

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
