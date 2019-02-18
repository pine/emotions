package moe.pine.emotions.gravatar.xmlrpc;


import com.google.common.collect.ImmutableMap;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImageFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

@ToString
@Slf4j
public class GravatarClient {
    private final String email;
    private final String endpoint;

    @ToString.Exclude
    private final XmlRpcClient rpcClient;

    public GravatarClient(String email) {
        this.email = email;
        this.endpoint = "https://secure.gravatar.com/xmlrpc?user=" + DigestUtils.md5Hex(email);

        URL endpointURL;
        try {
            endpointURL = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                String.format("Malformed URL format :: %s", endpoint), e);
        }

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setEncoding("UTF-8");
        config.setServerURL(endpointURL);

        XmlRpcClient rpcClient = new XmlRpcClient();
        rpcClient.setConfig(config);
        this.rpcClient = rpcClient;
    }

    public UserImage[] getUserImages(String password) throws GravatarClientException {
        Map<String, String> params = ImmutableMap.of("password", password);
        try {
            Object response = rpcClient.execute("grav.userimages", new Object[]{params});
            return UserImageFactory.fromArray(response);
        } catch (XmlRpcException e) {
            throw new GravatarClientException("Failed to call grav.userimages API", e);
        }
    }
}
