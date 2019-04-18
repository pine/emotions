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
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GravatarTest {
    private static String PASSWORD = "password";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private Random random;

    @Mock
    private GravatarClient gravatarClient;

    @Mock
    private GravatarClientException gravatarClientException;

    private Gravatar gravatar;

    @Before
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

    @SuppressWarnings("ConstantConditions")
    @Test
    public void constructorTest_nullRandom() {
        expectedException.expect(NullPointerException.class);

        new Gravatar(gravatarClient, PASSWORD, null);
    }

    @Test
    public void constructorTest_emptyPassword() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`password` should not be empty");

        new Gravatar(gravatarClient, "", random);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void constructorTest_nullPassword() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`password` should not be empty");

        new Gravatar(gravatarClient, null, random);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void constructorTest_nullGravatarClient() {
        expectedException.expect(NullPointerException.class);

        new Gravatar(null, PASSWORD, random);
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
        expectedException.expect(GravatarException.class);

        when(gravatarClient.getUserImages(PASSWORD))
            .thenThrow(gravatarClientException);

        gravatar.getUserImages();
    }

    @Test
    public void chooseImageTest() {
        // TODO
    }

    @Test
    public void chooseImageTest_emptyImages() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`images` should not be empty");

        gravatar.chooseImage(
            Collections.emptyList(),
            ImmutableList.of("example@example.com"));
    }

    @Test
    public void chooseImageTest_emptyAddresses() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`addresses` should not be empty");

        gravatar.chooseImage(
            ImmutableList.of("example@example.com"),
            Collections.emptyList());
    }
}
