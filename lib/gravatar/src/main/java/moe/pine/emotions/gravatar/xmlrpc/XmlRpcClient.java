package moe.pine.emotions.gravatar.xmlrpc;


import lombok.extern.slf4j.Slf4j;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClientConfig;

import java.util.Arrays;

@Slf4j
public class XmlRpcClient {
    private final org.apache.xmlrpc.client.XmlRpcClient underlying =
        new org.apache.xmlrpc.client.XmlRpcClient();

    public void setConfig(XmlRpcClientConfig pConfig) {
        underlying.setConfig(pConfig);
    }

    public Object execute(String pMethodName, Object[] pParams) throws XmlRpcException {
        log.debug(String.format("Execute XML-RPC :: method=%s, args=%s", pMethodName, Arrays.deepToString(pParams)));

        final Object response = underlying.execute(pMethodName, pParams);
        log.debug(String.format("Executed XML-RPC :: method=%s, response=%s", pMethodName, response.toString()));

        return response;
    }

}
