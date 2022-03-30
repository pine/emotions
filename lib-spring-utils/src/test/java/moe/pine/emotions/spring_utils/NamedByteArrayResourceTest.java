package moe.pine.emotions.spring_utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class NamedByteArrayResourceTest {
    @Test
    public void constructorTest() {
        final byte[] bytes = {0x01, 0x02, 0x03};
        final String filename = "abc.txt";
        final var resource = new NamedByteArrayResource(bytes, filename);

        assertEquals(filename, resource.getFilename());
        assertArrayEquals(bytes, resource.getByteArray());
    }

    @Test
    public void hashCodeTest() {
        final byte[] bytes = {0x01, 0x02, 0x03};
        final String filename = "abc.txt";
        final var resource1 = new NamedByteArrayResource(bytes, filename);
        final var resource2 = new NamedByteArrayResource(bytes, filename);

        assertNotSame(resource1, resource2);
        assertEquals(resource1.hashCode(), resource2.hashCode());
    }
}
