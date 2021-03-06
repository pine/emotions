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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("ConstantConditions")
@ExtendWith(MockitoExtension.class)
public class GravatarClientTest {
    private static final String EMAIL = "example@example.com";
    private static final String ENDPOINT = "https://secure.gravatar.com/xmlrpc?user=23463b99b62a72f26ed677cc556c44e8";
    private static final String PASSWORD = "password";

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

    @BeforeEach
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
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                new GravatarClient(StringUtils.EMPTY);
            });

        assertEquals("`email` should not be empty", exception.getMessage());
    }

    @Test
    public void constructorNullEmailTest() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                new GravatarClient(null);
            });

        assertEquals("`email` should not be empty", exception.getMessage());
    }

    @Test
    public void constructorNullClientExceptionTest() {
        assertThrows(NullPointerException.class, () -> {
            new GravatarClient("example@example.com", null, userImageFactory, statusFactory);
        });
    }

    @Test
    public void constructor_nullUserImageFactory() {
        assertThrows(NullPointerException.class, () -> {
            new GravatarClient(EMAIL, xmlRpcClient, null, statusFactory);
        });
    }

    @Test
    public void constructor_nullStatusFactory() {
        assertThrows(NullPointerException.class, () -> {
            new GravatarClient(EMAIL, xmlRpcClient, userImageFactory, null);
        });
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
        final Map<String, String> params = ImmutableMap.of("password", PASSWORD);
        final XmlRpcException exception = mock(XmlRpcException.class);

        when(xmlRpcClient.execute("grav.userimages", new Object[]{params}))
            .thenThrow(exception);

        assertThrows(GravatarClientException.class, () -> {
            gravatarClient.getUserImages(PASSWORD);
        });
    }

    @Test
    public void getUserImagesEmptyPasswordTest() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                gravatarClient.getUserImages(StringUtils.EMPTY);
            });

        assertEquals("`password` should not be empty", exception.getMessage());
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
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                gravatarClient.useUserImage(StringUtils.EMPTY, "userImage", Collections.singletonList(EMAIL));
            });

        assertEquals("`password` should not be empty", exception.getMessage());
    }

    @Test
    public void useUserImageNullPasswordTest() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                gravatarClient.useUserImage(null, "userImage", Collections.singletonList(EMAIL));
            });

        assertEquals("`password` should not be empty", exception.getMessage());
    }

    @Test
    public void useUserImageEmptyUserImageTest() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                gravatarClient.useUserImage(PASSWORD, StringUtils.EMPTY, Collections.singletonList(EMAIL));
            });

        assertEquals("`userImage` should not be empty", exception.getMessage());
    }

    @Test
    public void useUserImageEmptyAddressesTest() {
        final Exception exception =
            assertThrows(IllegalArgumentException.class, () -> {
                gravatarClient.useUserImage(PASSWORD, "userImage", List.of());
            });

        assertEquals("`addresses` should not be empty", exception.getMessage());
    }
}
