package moe.pine.emotions.gravatar;

import com.google.common.collect.ImmutableList;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClientException;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
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
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    public GravatarClient gravatarClient;

    @Mock
    public GravatarClientException gravatarClientException;

    @Test
    public void constructorTest() {
        final GravatarClient gravatarClient = new GravatarClient("example@example.com");
        final Gravatar gravatar = new Gravatar(gravatarClient, "password");
        assertSame(gravatarClient, gravatar.getGravatarClient());
        assertEquals("password", gravatar.getPassword());
    }

    @Test
    public void constructorEmptyPasswordTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`password` should not be empty");

        new Gravatar(gravatarClient, "");
    }

    @Test
    public void constructorNullGravatarClientTest() {
        expectedException.expect(NullPointerException.class);

        //noinspection ConstantConditions
        new Gravatar(null, "password");
    }

    @Test
    public void getUserImagesTest() {
        final Gravatar gravatar = new Gravatar(gravatarClient, "password");

        final List<UserImage> userImages = Collections.emptyList();
        when(gravatarClient.getUserImages("password")).thenReturn(userImages);

        assertSame(userImages, gravatar.getUserImages());
        verify(gravatarClient, times(1)).getUserImages("password");
    }

    @Test
    public void getUserImagesClientExceptionTest() {
        expectedException.expect(GravatarException.class);

        final Gravatar gravatar = new Gravatar(gravatarClient, "password");
        when(gravatarClient.getUserImages("password")).thenThrow(gravatarClientException);

        gravatar.getUserImages();
    }

    @Test
    public void chooseImageEmptyImagesTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`images` should not be empty");

        final Gravatar gravatar = new Gravatar(gravatarClient, "password");
        gravatar.chooseImage(
            Collections.emptyList(),
            ImmutableList.of("example@example.com"));
    }

    @Test
    public void chooseImageEmptyAddressesTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`addresses` should not be empty");

        final Gravatar gravatar = new Gravatar(gravatarClient, "password");
        gravatar.chooseImage(
            ImmutableList.of("example@example.com"),
            Collections.emptyList());
    }
}
