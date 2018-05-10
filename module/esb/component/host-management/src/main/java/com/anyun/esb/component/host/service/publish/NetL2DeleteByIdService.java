package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.NetL2InfoService;
import com.anyun.esb.component.host.service.docker.impl.NetL2InfoServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/4/11.
 */
public class NetL2DeleteByIdService extends AbstractBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetLabelInfoDeleteByLabelService.class);
    private NetL2InfoService service;

    public NetL2DeleteByIdService() {
        service = new NetL2InfoServiceImpl();
    }

    @Override
    public String getName() {
        return "netL2Info_delete_by_id";
    }

    @Override
    public String getDescription() {
        return "NetL2DeleteByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
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

        String id = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("id is empty");
            exception.setMessage("id is empty");
            exception.setUserTitle("id is empty");
            throw exception;
        }

        try {
            service.netL2DeleteById(id);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("delete NetL2 fail");
            exception.setMessage("delete NetL2  error" + e.getMessage());
            exception.setUserTitle("L2网络删除失败");
            throw exception;
        }
    }
}
