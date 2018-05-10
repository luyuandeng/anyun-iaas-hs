package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.ConnectToNetParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.NetService;
import com.anyun.esb.component.host.service.docker.impl.NetServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-7-22.
 */
public class ContainerConnectToNetworkService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerConnectToNetworkService.class);
    private NetService service;

    public ContainerConnectToNetworkService() {
        service = new NetServiceImpl();
    }

    @Override
    public String getName() {
        return "container_connect_to_network";
    }

    @Override
    public String getDescription() {
        return "Container Connect To Network Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("postBody is empty");
            throw exception;
        }
        ConnectToNetParam param = JsonUtil.fromJson(ConnectToNetParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("param is empty");
            throw exception;
        }

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        try {
            service.containerConnectToNetwork(param);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Container Connect To Network error [" + e.getMessage() + "]");
            exception.setUserMessage("虚拟机绑到网络失败： [" + e.getMessage() + "]");
            exception.setUserTitle("虚拟机绑到网络失败");
            throw exception;
        }
    }
}
