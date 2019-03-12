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

    void setConfig(XmlRpcClientConfig pConfig) {
        underlying.setConfig(pConfig);
    }

    XmlRpcConfig getConfig() {
        return underlying.getConfig();
    }

    Object execute(String pMethodName, Object[] pParams) throws XmlRpcException {
        log.debug("Execute XML-RPC :: method={}, args={}", pMethodName, Arrays.deepToString(pParams));

        final Object response = underlying.execute(pMethodName, pParams);
        log.debug("Executed XML-RPC :: method={}, response={}", pMethodName, response.toString());

        return response;
    }

}
