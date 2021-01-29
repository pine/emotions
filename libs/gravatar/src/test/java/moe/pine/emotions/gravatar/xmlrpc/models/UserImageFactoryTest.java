package moe.pine.emotions.gravatar.xmlrpc.models;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SuppressWarnings({
    "ConstantConditions",
    "ZeroLengthArrayAllocation",
})
@ExtendWith(MockitoExtension.class)
public class UserImageFactoryTest {

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
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                userImageFactory.from(List.<Integer>of());
            });

        assertTrue(exception.getMessage().startsWith("Unexpected data format ::"));
    }

    @Test
    public void fromNullTest() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                userImageFactory.from(null);
            });

        assertEquals("Unexpected data format :: null", exception.getMessage());
    }

    @Test
    public void fromInvalidKeyTest() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                userImageFactory.from(Map.of(1, new Object[]{0, "foo"}));
            });

        assertTrue(exception.getMessage().startsWith("Unexpected data format ::"));
    }

    @Test
    public void fromInvalidValueTest() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                userImageFactory.from(Map.of("foo", true));
            });

        assertTrue(exception.getMessage().startsWith("Unexpected data format ::"));
    }

    @Test
    public void fromNullValueTest() {
        final Map<String, Object> data = Maps.newHashMap();
        data.put("foo", null);

        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                userImageFactory.from(data);
            });

        assertTrue(exception.getMessage().startsWith("Unexpected data format ::"));
    }

    @Test
    public void fromInvalidValueLengthTest() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                userImageFactory.from(Map.of("foo", new Object[]{}));
            });

        assertEquals("Unexpected array length :: expected=2, actual=0", exception.getMessage());
    }

    @Test
    public void fromInvalidValueTypeTest() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                userImageFactory.from(Map.of("foo", new Object[]{0, 1}));
            });

        assertTrue(exception.getMessage().startsWith("Unexpected data format :: "));
    }

}
