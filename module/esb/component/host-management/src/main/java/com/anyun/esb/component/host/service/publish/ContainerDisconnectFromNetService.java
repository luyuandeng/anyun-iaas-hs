package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.DisconnectFromNetParam;
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
 * Created by sxt on 16-7-25.
 */
public class ContainerDisconnectFromNetService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerDisconnectFromNetService.class);
    private NetService service;

    public ContainerDisconnectFromNetService() {
        service = new NetServiceImpl();
    }

    @Override
    public String getName() {
        return "container_disconnect_from_network";
    }

    @Override
    public String getDescription() {
        return "Container Disconnect FromNet Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        if (StringUtils.isEmpty(postBody)) {
            LOGGER.debug("postBody is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("postBody is empty");
            throw exception;
        }
        DisconnectFromNetParam param = JsonUtil.fromJson(DisconnectFromNetParam.class, postBody);
        if (param == null) {
            LOGGER.debug("param  is  null ");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param  is  null");
            exception.setUserMessage("param  is  null");
            exception.setUserTitle("param  is  null");
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
            LOGGER.debug("param  is:{}", param.asJson());
            service.containerDisconnectFromNetwork(param);
            return new Status<String>("success");
        } catch (Exception e) {
            LOGGER.debug("errorMsg  is  :{}", e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Container  Disconnect  FromNet  error [" + e.getMessage() + "]");
            exception.setUserMessage("将虚拟机从OVN标签移除： [" + e.getMessage() + "]");
            exception.setUserTitle("将虚拟机从OVN标签移除");
            throw exception;
        }
    }
}
