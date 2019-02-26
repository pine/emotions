package moe.pine.emotions.gravatar.xmlrpc.models;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StatusFactoryTest {
    @Test
    public void fromTest() {
        final Map<String, Boolean> expected =
            ImmutableMap.of(
                "foo", true,
                "bar", false
            );

        final Map<String, Boolean> actual = StatusFactory.from(expected);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromNonMapTest() {
        StatusFactory.from(Collections.<Integer>emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidKeyTest() {
        StatusFactory.from(ImmutableMap.of(1, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidValueTest() {
        StatusFactory.from(ImmutableMap.of("foo", 1));
    }
}
