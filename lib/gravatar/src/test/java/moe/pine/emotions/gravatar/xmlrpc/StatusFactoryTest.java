package moe.pine.emotions.gravatar.xmlrpc;

import com.google.common.collect.ImmutableMap;
import moe.pine.emotions.gravatar.xmlrpc.models.StatusFactory;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StatusFactoryTest {
    @Test
    public void fromArrayTest() {
        final Map<String, Boolean> expected =
            ImmutableMap.of(
                "foo", true,
                "bar", false
            );

        final Map<String, Boolean> actual = StatusFactory.fromArray(expected);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromArrayNonMapTest() {
        StatusFactory.fromArray(Collections.<Integer>emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromArrayInvalidKeyTest() {
        StatusFactory.fromArray(ImmutableMap.of(1, true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromArrayInvalidValueTest() {
        StatusFactory.fromArray(ImmutableMap.of("foo", 1));
    }
}
