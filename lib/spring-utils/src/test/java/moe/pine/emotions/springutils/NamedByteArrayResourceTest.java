package moe.pine.emotions.springutils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class NamedByteArrayResourceTest {
    @Test
    public void constructorTest() {
        final byte[] bytes = new byte[]{0x01, 0x02, 0x03};
        final String filename = "abc.txt";
        final var resource = new NamedByteArrayResource(bytes, filename);

        assertEquals(filename, resource.getFilename());
        assertEquals(bytes, resource.getByteArray());
    }

    @Test
    public void hashCodeTest() {
        final byte[] bytes = new byte[]{0x01, 0x02, 0x03};
        final String filename = "abc.txt";
        final var resource1 = new NamedByteArrayResource(bytes, filename);
        final var resource2 = new NamedByteArrayResource(bytes, filename);

        assertNotSame(resource1, resource2);
        assertEquals(resource1.hashCode(), resource2.hashCode());
    }
}
