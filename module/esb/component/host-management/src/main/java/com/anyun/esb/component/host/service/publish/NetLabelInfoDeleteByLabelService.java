package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.tools.StringUtils;
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
public class NetLabelInfoDeleteByLabelService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetLabelInfoDeleteByLabelService.class);
    private NetService service;

    public NetLabelInfoDeleteByLabelService() {
        service = new NetServiceImpl();
    }

    @Override
    public String getName() {
        return "netLabelInfo_delete_by_label";
    }

    @Override
    public String getDescription() {
        return "NetLabelInfo Delete By Label Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String id = exchange.getIn().getHeader("id", String.class);
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"DELETE");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String label = exchange.getIn().getHeader("label", String.class);
        LOGGER.debug("label:[{}]", label);
        if (StringUtils.isEmpty(label)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("label is empty");
            exception.setMessage("label is empty");
            exception.setUserTitle("label is empty");
            throw exception;
        }

        try {
            service.netLabelInfoDeleteByLabel(label);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("delete label fail");
            exception.setMessage("delete label  error" + e.getMessage());
            exception.setUserTitle("删除标签失败");
            throw exception;
        }
    }
}
