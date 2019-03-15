package moe.pine.emotions.gravatar.xmlrpc.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserImageFactoryTest {
    private UserImageFactory userImageFactory;

    @Before
    public void setUp() {
        userImageFactory = new UserImageFactory();
    }

    @Test
    public void fromTest() {
        final List<UserImage> expected = ImmutableList.of(
            UserImage.builder()
                .hash("foo")
                .url("https://www.example.com/images/foo.png")
                .build()
        );

        final Map<String, Object[]> data = ImmutableMap.of(
            "foo", new Object[]{0, "https://www.example.com/images/foo.png"}
        );
        final List<UserImage> actual = userImageFactory.from(data);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromNonMapTest() {
        userImageFactory.from(Collections.<Integer>emptyList());
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidKeyTest() {
        userImageFactory.from(ImmutableMap.of(1, new Object[]{0, "foo"}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidValueTest() {
        userImageFactory.from(ImmutableMap.of("foo", true));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidValueLengthTest() {
        userImageFactory.from(ImmutableMap.of("foo", new Object[]{}));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromInvalidValueTypeTest() {
        userImageFactory.from(ImmutableMap.of("foo", new Object[]{0, 1}));
    }

}
