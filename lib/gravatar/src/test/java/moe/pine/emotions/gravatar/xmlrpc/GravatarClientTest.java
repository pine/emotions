package moe.pine.emotions.gravatar.xmlrpc;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.SneakyThrows;
import moe.pine.emotions.gravatar.xmlrpc.models.StatusFactory;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImageFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("NullableProblems")
public class GravatarClientTest {
    private static final String EMAIL = "example@example.com";
    private static final String ENDPOINT = "https://secure.gravatar.com/xmlrpc?user=23463b99b62a72f26ed677cc556c44e8";
    private static final String PASSWORD = "password";

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Mock
    private XmlRpcClient xmlRpcClient;

    @Mock
    private UserImageFactory userImageFactory;

    @Mock
    private StatusFactory statusFactory;

    private GravatarClient gravatarClient;

    @Captor
    private ArgumentCaptor<XmlRpcClientConfigImpl> configCaptor;

    @Captor
    private ArgumentCaptor<Object> rpcResultCaptor;

    @Before
    public void setUp() {
        doNothing().when(xmlRpcClient).setConfig(configCaptor.capture());
        gravatarClient = new GravatarClient(EMAIL, xmlRpcClient, userImageFactory, statusFactory);
    }

    @Test
    public void constructorTest() {
        assertEquals(EMAIL, gravatarClient.getEmail());
        assertEquals(ENDPOINT, gravatarClient.getEndpoint());
        assertSame(xmlRpcClient, gravatarClient.getRpcClient());

        final XmlRpcClientConfigImpl config = configCaptor.getValue();
        assertEquals("UTF-8", config.getEncoding());
        assertEquals(ENDPOINT, config.getServerURL().toString());
    }

    @Test
    public void constructorEmptyEmailTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`email` should not be empty");

        new GravatarClient(StringUtils.EMPTY);
    }

    @Test
    public void constructorNullEmailTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`email` should not be empty");

        //noinspection ConstantConditions
        new GravatarClient(null);
    }

    @Test
    public void constructorNullClientExceptionTest() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("`rpcClient` should not be empty");

        //noinspection ConstantConditions
        new GravatarClient("example@example.com", null, userImageFactory, statusFactory);
    }

    @Test
    @SneakyThrows
    public void getUserImagesPasswordTest() {
        final Map<String, String> params = ImmutableMap.of("password", PASSWORD);
        final Object rpcResult = new Object();
        final List<UserImage> expected = Collections.emptyList();

        when(xmlRpcClient.execute("grav.userimages", new Object[]{params}))
            .thenReturn(rpcResult);
        when(userImageFactory.from(rpcResultCaptor.capture())).thenReturn(expected);

        final List<UserImage> actual = gravatarClient.getUserImages(PASSWORD);

        assertSame(rpcResult, rpcResultCaptor.getValue());
        assertSame(expected, actual);

        verify(xmlRpcClient).execute("grav.userimages", new Object[]{params});
        verify(userImageFactory).from(any());
    }

    @Test
    @SneakyThrows
    public void getUserImagesFailedTest() {
        expectedException.expect(GravatarClientException.class);

        final Map<String, String> params = ImmutableMap.of("password", PASSWORD);
        final XmlRpcException exception = mock(XmlRpcException.class);

        when(xmlRpcClient.execute("grav.userimages", new Object[]{params}))
            .thenThrow(exception);

        gravatarClient.getUserImages(PASSWORD);
    }

    @Test
    public void getUserImagesEmptyPasswordTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`password` should not be empty");

        gravatarClient.getUserImages(StringUtils.EMPTY);
    }

    @Test
    @SneakyThrows
    public void useUserImageTest() {
        final Map<String, Object> params = ImmutableMap.of(
            "password", PASSWORD,
            "userimage", "userImage",
            "addresses", Collections.singletonList(EMAIL)
        );
        final Object rpcResult = new Object();
        final Map<String, Boolean> expected = Maps.newHashMap();

        when(xmlRpcClient.execute("grav.useUserimage", new Object[]{params}))
            .thenReturn(rpcResult);
        when(statusFactory.from(rpcResultCaptor.capture())).thenReturn(expected);

        final Map<String, Boolean> actual =
            gravatarClient.useUserImage(PASSWORD, "userImage", Collections.singletonList(EMAIL));

        assertSame(expected, actual);
        assertSame(rpcResult, rpcResultCaptor.getValue());

        verify(xmlRpcClient).execute(anyString(), any());
        verify(statusFactory).from(any());
    }

    @Test
    public void useUserImageEmptyPasswordTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`password` should not be empty");

        gravatarClient.useUserImage(StringUtils.EMPTY, "userImage", Collections.singletonList(EMAIL));
    }

    @Test
    public void useUserImageNullPasswordTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`password` should not be empty");

        //noinspection ConstantConditions
        gravatarClient.useUserImage(null, "userImage", Collections.singletonList(EMAIL));
    }

    @Test
    public void useUserImageEmptyUserImageTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`userImage` should not be empty");

        gravatarClient.useUserImage(PASSWORD, StringUtils.EMPTY, Collections.singletonList(EMAIL));
    }

    @Test
    public void useUserImageEmptyAddressesTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`addresses` should not be empty");

        gravatarClient.useUserImage(PASSWORD, "userImage", Collections.emptyList());
    }

}
