package moe.pine.emotions.gravatar.xmlrpc.models;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;

public class UserImageFactoryTest {
    @Test
    public void fromTest() {
        final UserImage[] expected = new UserImage[]{
            UserImage.builder()
                .hash("foo")
                .url("https://www.example.com/images/foo.png")
                .build()
        };

        final Map<String, Object[]> data = ImmutableMap.of(
            "foo", new Object[]{0, "https://www.example.com/images/foo.png"}
        );
        final UserImage[] actual = UserImageFactory.from(data);

        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromNonMapTest() {
        UserImageFactory.from(Collections.<Integer>emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidKeyTest() {
        UserImageFactory.from(ImmutableMap.of(1, new Object[]{0, "foo"}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidValueTest() {
        UserImageFactory.from(ImmutableMap.of("foo", true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidValueLengthTest() {
        UserImageFactory.from(ImmutableMap.of("foo", new Object[]{}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidValueTypeTest() {
        UserImageFactory.from(ImmutableMap.of("foo", new Object[]{0, 1}));
    }

}
