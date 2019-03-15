package moe.pine.emotions.gravatar.xmlrpc;

import com.google.common.collect.ImmutableMap;
import lombok.SneakyThrows;
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
import static org.mockito.Mockito.*;

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

    private GravatarClient gravatarClient;

    @Captor
    private ArgumentCaptor<XmlRpcClientConfigImpl> configCaptor;

    @Captor
    private ArgumentCaptor<Object> rpcResultCaptor;

    @Before
    public void setUp() {
        doNothing().when(xmlRpcClient).setConfig(configCaptor.capture());
        gravatarClient = new GravatarClient(EMAIL, xmlRpcClient, userImageFactory);
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
    public void constructorExceptionTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`email` should not be empty");

        //noinspection ConstantConditions
        new GravatarClient(StringUtils.EMPTY);
    }

    @Test
    public void constructorNPEExceptionTest() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("`rpcClient` should not be empty");

        //noinspection ConstantConditions
        new GravatarClient("example@example.com", null, userImageFactory);
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
        expectedException.expectMessage("`password` cannot be empty");

        gravatarClient.getUserImages(StringUtils.EMPTY);
    }
}
