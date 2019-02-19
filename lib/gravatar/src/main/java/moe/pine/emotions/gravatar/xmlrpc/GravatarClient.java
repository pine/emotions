package moe.pine.emotions.gravatar.xmlrpc;


import com.google.common.collect.ImmutableMap;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImageFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import javax.annotation.Nonnull;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;

@ToString
@Slf4j
public class GravatarClient {
    @Nonnull
    private final String email;

    @Nonnull
    private final String endpoint;

    @ToString.Exclude
    private final XmlRpcClient rpcClient;

    public GravatarClient(@Nonnull final String email) {
        checkArgument(StringUtils.isNotEmpty(email), "`email` cannot be empty");

        this.email = email;
        this.endpoint = "https://secure.gravatar.com/xmlrpc?user=" + DigestUtils.md5Hex(email);

        final URL endpointURL;
        try {
            endpointURL = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                String.format("Malformed URL format :: %s", endpoint), e);
        }

        final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setEncoding("UTF-8");
        config.setServerURL(endpointURL);

        final XmlRpcClient rpcClient = new XmlRpcClient();
        rpcClient.setConfig(config);
        this.rpcClient = rpcClient;
    }

    @Nonnull
    public UserImage[] getUserImages(@Nonnull final String password) throws GravatarClientException {
        checkArgument(StringUtils.isNotEmpty(password), "`password` cannot be empty");

        final Map<String, String> params = ImmutableMap.of("password", password);
        try {
            final Object response = rpcClient.execute("grav.userimages", new Object[]{params});
            return UserImageFactory.fromArray(response);
        } catch (XmlRpcException e) {
            throw new GravatarClientException("Failed to call grav.userimages API", e);
        }
    }
}
