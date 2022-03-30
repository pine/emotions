package moe.pine.emotions.gravatar.xmlrpc.models;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatusFactoryTest {
    private StatusFactory statusFactory;

    @BeforeEach
    public void setUp() {
        statusFactory = new StatusFactory();
    }

    @Test
    public void fromTest() {
        final Map<String, Boolean> expected =
            ImmutableMap.of(
                "foo", true,
                "bar", false
            );

        final Map<String, Boolean> actual = statusFactory.from(expected);
        assertEquals(expected, actual);
    }

    @Test
    public void fromNonMapTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            statusFactory.from(Collections.<Integer>emptyList());
        });
    }

    @Test
    public void fromInvalidKeyTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            statusFactory.from(ImmutableMap.of(1, true));
        });
    }

    @Test
    public void fromInvalidValueTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            statusFactory.from(ImmutableMap.of("foo", 1));
        });
    }
}
