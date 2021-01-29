package moe.pine.emotions.gravatar;

import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClientException;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("ConstantConditions")
@ExtendWith(MockitoExtension.class)
public class GravatarTest {
    private static final String PASSWORD = "password";

    @Mock
    private Random random;

    @Mock
    private GravatarClient gravatarClient;

    @Mock
    private GravatarClientException gravatarClientException;

    private Gravatar gravatar;

    @BeforeEach
    public void setUp() {
        gravatar = new Gravatar(gravatarClient, PASSWORD, random);
    }

    @Test
    public void constructorTest() {
        final GravatarClient gravatarClient = new GravatarClient("example@example.com");
        final Gravatar gravatar = new Gravatar(gravatarClient, PASSWORD, random);
        assertSame(random, gravatar.getRandom());
        assertSame(gravatarClient, gravatar.getGravatarClient());
        assertEquals(PASSWORD, gravatar.getPassword());
    }

    @Test
    public void constructorTest_nullRandom() {
        assertThrows(NullPointerException.class, () -> {
            new Gravatar(gravatarClient, PASSWORD, null);
        });
    }

    @Test
    public void constructorTest_emptyPassword() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                new Gravatar(gravatarClient, "", random);
            });

        assertEquals("`password` should not be empty", exception.getMessage());
    }

    @Test
    public void constructorTest_nullPassword() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                new Gravatar(gravatarClient, null, random);
            });

        assertEquals("`password` should not be empty", exception.getMessage());
    }

    @Test
    public void constructorTest_nullGravatarClient() {
        assertThrows(NullPointerException.class, () -> {
            new Gravatar(null, PASSWORD, random);
        });
    }

    @Test
    public void getUserImagesTest() {
        final List<UserImage> userImages = Collections.emptyList();
        when(gravatarClient.getUserImages(PASSWORD)).thenReturn(userImages);

        assertSame(userImages, gravatar.getUserImages());
        verify(gravatarClient).getUserImages(PASSWORD);
    }

    @Test
    public void getUserImagesClientExceptionTest() {
        when(gravatarClient.getUserImages(PASSWORD)).thenThrow(gravatarClientException);

        assertThrows(GravatarException.class, () -> {
            gravatar.getUserImages();
        });
    }

    @Test
    public void chooseImageTest() {
        // TODO
    }

    @Test
    public void chooseImageTest_emptyImages() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                gravatar.chooseImage(List.of(), List.of("example@example.com"));
            });

        assertEquals("`images` should not be empty", exception.getMessage());

    }

    @Test
    public void chooseImageTest_emptyAddresses() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                gravatar.chooseImage(List.of("example@example.com"), List.of());
            });

        assertEquals("`addresses` should not be empty", exception.getMessage());
    }
}
