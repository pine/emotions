package moe.pine.emotions.gravatar;

import moe.pine.emotions.gravatar.xmlrpc.GravatarClient;
import moe.pine.emotions.gravatar.xmlrpc.GravatarClientException;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class GravatarTest {
    @Rule
    public MockitoRule mockito = MockitoJUnit.rule();

    @Mock
    public GravatarClient gravatarClient;

    @Test
    public void constructorTest() {
        final GravatarClient gravatarClient = new GravatarClient("example@example.com");
        final Gravatar gravatar = new Gravatar(gravatarClient, "password");
        assertSame(gravatarClient, gravatar.getGravatarClient());
        assertEquals("password", gravatar.getPassword());
    }

    @Test
    public void getUserImagesTest() throws Exception {
        final Gravatar gravatar = new Gravatar(gravatarClient, "password");

        final UserImage[] userImages = new UserImage[]{};
        when(gravatarClient.getUserImages("password")).thenReturn(userImages);

        assertSame(userImages, gravatar.getUserImages());
        verify(gravatarClient, times(1)).getUserImages("password");
    }

    @Test(expected = GravatarException.class)
    public void getUserImagesExceptionTest() throws Exception {
        final Gravatar gravatar = new Gravatar(gravatarClient, "password");

        final Throwable throwable = new Exception();
        final GravatarClientException exception = new GravatarClientException("error", throwable);
        when(gravatarClient.getUserImages("password")).thenThrow(exception);

        gravatar.getUserImages();
    }
}
