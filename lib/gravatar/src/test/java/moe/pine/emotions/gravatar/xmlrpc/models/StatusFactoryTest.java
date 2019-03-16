package moe.pine.emotions.gravatar.xmlrpc.models;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StatusFactoryTest {
    private StatusFactory statusFactory;

    @Before
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

    @Test(expected = IllegalArgumentException.class)
    public void fromNonMapTest() {
        statusFactory.from(Collections.<Integer>emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidKeyTest() {
        statusFactory.from(ImmutableMap.of(1, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidValueTest() {
        statusFactory.from(ImmutableMap.of("foo", 1));
    }
}
