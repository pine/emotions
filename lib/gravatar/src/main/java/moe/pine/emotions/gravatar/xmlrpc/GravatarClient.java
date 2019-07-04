package moe.pine.emotions.gravatar.xmlrpc;


import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import moe.pine.emotions.gravatar.xmlrpc.models.StatusFactory;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImage;
import moe.pine.emotions.gravatar.xmlrpc.models.UserImageFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @see <a href="https://ja.gravatar.com/site/implement/xmlrpc/">XML-RPC API</a>
 */
@ToString
@Slf4j
@Getter
public class GravatarClient {
    private final String email;
    private final String endpoint;

    @ToString.Exclude
    private final XmlRpcClient rpcClient;

    @ToString.Exclude
    private final UserImageFactory userImageFactory;

    @ToString.Exclude
    private final StatusFactory statusFactory;

    public GravatarClient(final String email) {
        this(email, new XmlRpcClient(), new UserImageFactory(), new StatusFactory());
    }

    protected GravatarClient(
        final String email,
        final XmlRpcClient rpcClient,
        final UserImageFactory userImageFactory,
        final StatusFactory statusFactory
    ) {
        checkArgument(StringUtils.isNotEmpty(email), "`email` should not be empty");
        Objects.requireNonNull(rpcClient);

        this.email = email;
        this.endpoint = "https://secure.gravatar.com/xmlrpc?user=" + DigestUtils.md5Hex(email);
        this.userImageFactory = Objects.requireNonNull(userImageFactory);
        this.statusFactory = Objects.requireNonNull(statusFactory);

        final URL endpointUrl;
        try {
            endpointUrl = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                String.format("Malformed URL format :: %s", endpoint), e);
        }

        final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setEncoding("UTF-8");
        config.setServerURL(endpointUrl);

        rpcClient.setConfig(config);
        this.rpcClient = rpcClient;
    }

    /**
     * grav.userimages
     */
    public List<UserImage> getUserImages(final String password) {
        checkArgument(StringUtils.isNotEmpty(password), "`password` should not be empty");

        final Map<String, String> params = ImmutableMap.of("password", password);
        try {
            final Object response = rpcClient.execute("grav.userimages", new Object[]{params});
            return userImageFactory.from(response);
        } catch (XmlRpcException e) {
            throw new GravatarClientException("Failed to call `grav.userimages` API", e);
        }
    }

    /**
     * grav.useUserimage
     */
    public Map<String, Boolean> useUserImage(
        final String password,
        final String userImage,
        final List<String> addresses
    ) {
        checkArgument(StringUtils.isNotEmpty(password), "`password` should not be empty");
        checkArgument(StringUtils.isNotEmpty(userImage), "`userImage` should not be empty");
        checkArgument(CollectionUtils.isNotEmpty(addresses), "`addresses` should not be empty");

        final Map<String, Object> params = ImmutableMap.of(
            "password", password,
            "userimage", userImage,
            "addresses", addresses
        );
        try {
            final Object response = rpcClient.execute("grav.useUserimage", new Object[]{params});
            return statusFactory.from(response);
        } catch (XmlRpcException e) {
            throw new GravatarClientException("Failed to call `grav.useUserimage` API", e);
        }
    }
}
