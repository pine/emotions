package moe.pine.emotions.gravatar.xmlrpc;

import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class GravatarClientTest {
    private static final String EMAIL = "example@example.com";
    private static final String ENDPOINT = "https://secure.gravatar.com/xmlrpc?user=23463b99b62a72f26ed677cc556c44e8";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

    @Test
    public void constructorExceptionTest() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("`email` should not be empty");

        //noinspection ConstantConditions
        new GravatarClient(null);
    }

    @Test
    public void constructorNPEExceptionTest() {
        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("`rpcClient` should not be empty");

        //noinspection ConstantConditions
        new GravatarClient("example@example.com", null);
    }
}
