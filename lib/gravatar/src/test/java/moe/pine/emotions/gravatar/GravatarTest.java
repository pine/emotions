package moe.pine.emotions.gravatar;

import com.google.common.collect.ImmutableList;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClientException;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class GravatarTest {
    private static String PASSWORD = "password";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    public GravatarClient gravatarClient;

    @Mock
    public GravatarClientException gravatarClientException;

    private Gravatar gravatar;

    @Before
    public void setUp() {
        gravatar = new Gravatar(gravatarClient, PASSWORD);
    }

    @Test
    public void constructorTest() {
        final GravatarClient gravatarClient = new GravatarClient("example@example.com");
        final Gravatar gravatar = new Gravatar(gravatarClient, PASSWORD);
        assertSame(gravatarClient, gravatar.getGravatarClient());
        assertEquals(PASSWORD, gravatar.getPassword());
    }

    @Test
    public void constructorEmptyPasswordTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`password` should not be empty");

        new Gravatar(gravatarClient, "");
    }

    @Test
    public void constructorNullPasswordTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`password` should not be empty");

        //noinspection ConstantConditions
        new Gravatar(gravatarClient, null);
    }

    @Test
    public void constructorNullGravatarClientTest() {
        expectedException.expect(NullPointerException.class);

        //noinspection ConstantConditions
        new Gravatar(null, PASSWORD);
    }

    @Test
    public void getUserImagesTest() {
        final List<UserImage> userImages = Collections.emptyList();
        when(gravatarClient.getUserImages(PASSWORD)).thenReturn(userImages);

        assertSame(userImages, gravatar.getUserImages());
        verify(gravatarClient, times(1)).getUserImages(PASSWORD);
    }

    @Test
    public void getUserImagesClientExceptionTest() {
        expectedException.expect(GravatarException.class);

        when(gravatarClient.getUserImages(PASSWORD)).thenThrow(gravatarClientException);

        gravatar.getUserImages();
    }

    @Test
    public void chooseImageEmptyImagesTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`images` should not be empty");

        gravatar.chooseImage(
            Collections.emptyList(),
            ImmutableList.of("example@example.com"));
    }

    @Test
    public void chooseImageEmptyAddressesTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`addresses` should not be empty");

        gravatar.chooseImage(
            ImmutableList.of("example@example.com"),
            Collections.emptyList());
    }
}
