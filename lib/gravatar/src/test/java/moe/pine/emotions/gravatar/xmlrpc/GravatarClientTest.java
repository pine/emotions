package moe.pine.emotions.gravatar.xmlrpc;

import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class GravatarClientTest {
    private static final String EMAIL = "example@example.com";
    private static final String ENDPOINT = "https://secure.gravatar.com/xmlrpc?user=23463b99b62a72f26ed677cc556c44e8";

    @Test
    public void constructorTest() {
        final XmlRpcClient rpcClient = new XmlRpcClient();
        final GravatarClient gravatarClient = new GravatarClient(EMAIL, rpcClient);
        assertEquals(EMAIL, gravatarClient.getEmail());
        assertEquals(ENDPOINT, gravatarClient.getEndpoint());
        assertSame(rpcClient, gravatarClient.getRpcClient());

        final XmlRpcClientConfigImpl config = (XmlRpcClientConfigImpl) rpcClient.getConfig();
        assertEquals("UTF-8", config.getEncoding());
        assertEquals(ENDPOINT, config.getServerURL().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    @SuppressWarnings("ConstantConditions")
    public void constructorExceptionTest() {
        new GravatarClient(null);
    }

    @Test(expected = NullPointerException.class)
    @SuppressWarnings("ConstantConditions")
    public void constructorNPEExceptionTest() {
        new GravatarClient("example@example.com", null);
    }
}
