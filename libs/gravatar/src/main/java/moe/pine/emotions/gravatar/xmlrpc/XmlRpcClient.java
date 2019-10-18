package moe.pine.emotions.gravatar.xmlrpc;


import com.google.common.annotations.VisibleForTesting;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlrpc.XmlRpcConfig;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClientConfig;

import java.util.Arrays;

@Slf4j
class XmlRpcClient {
    private final org.apache.xmlrpc.client.XmlRpcClient underlying;

    XmlRpcClient() {
        this(new org.apache.xmlrpc.client.XmlRpcClient());
    }

    @SuppressWarnings("WeakerAccess")
    @VisibleForTesting
    XmlRpcClient(org.apache.xmlrpc.client.XmlRpcClient rpcClient) {
        underlying = rpcClient;
    }

    void setConfig(XmlRpcClientConfig config) {
        underlying.setConfig(config);
    }

    XmlRpcConfig getConfig() {
        return underlying.getConfig();
    }

    Object execute(String methodName, Object[] params) throws XmlRpcException {
        log.debug("Execute XML-RPC :: method={}, args={}", methodName, Arrays.deepToString(params));

        final Object response = underlying.execute(methodName, params);
        log.debug("Executed XML-RPC :: method={}, response={}", methodName, response.toString());

        return response;
    }

}
