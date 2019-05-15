package moe.pine.emotions.gravatar.xmlrpc.models;

import com.google.common.collect.Maps;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class UserImageFactoryTest {
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @InjectMocks
    private UserImageFactory userImageFactory;

    @Test
    public void fromTest() {
        final List<UserImage> expected = List.of(
            UserImage.builder()
                .hash("foo")
                .url("https://www.example.com/images/foo.png")
                .build()
        );

        final Map<String, Object[]> data = Map.of(
            "foo", new Object[]{0, "https://www.example.com/images/foo.png"}
        );
        final List<UserImage> actual = userImageFactory.from(data);

        assertEquals(expected, actual);
    }

    @Test
    public void fromNonMapTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unexpected data format :: ");

        userImageFactory.from(Collections.<Integer>emptyList());
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void fromNullTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unexpected data format :: null");

        userImageFactory.from(null);
    }

    @Test
    public void fromInvalidKeyTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unexpected data format ::");

        userImageFactory.from(Map.of(1, new Object[]{0, "foo"}));
    }

    @Test
    public void fromInvalidValueTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unexpected data format ::");

        userImageFactory.from(Map.of("foo", true));
    }

    @Test
    public void fromNullValueTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unexpected data format ::");

        final Map<String, Object> data = Maps.newHashMap();
        data.put("foo", null);

        userImageFactory.from(data);
    }

    @Test
    public void fromInvalidValueLengthTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unexpected array length :: expected=2, actual=0");

        userImageFactory.from(Map.of("foo", new Object[]{}));
    }

    @Test
    public void fromInvalidValueTypeTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Unexpected data format :: ");

        userImageFactory.from(Map.of("foo", new Object[]{0, 1}));
    }

}
