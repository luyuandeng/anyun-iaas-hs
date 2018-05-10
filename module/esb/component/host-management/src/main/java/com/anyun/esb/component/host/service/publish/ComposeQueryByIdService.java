package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ComposeDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.ComposeService;
import com.anyun.esb.component.host.service.docker.impl.ComposeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-12-6.
 */
public class ComposeQueryByIdService  extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComposeQueryByIdService.class);
    private ComposeService composeService;

    public ComposeQueryByIdService() {
        composeService = new ComposeServiceImpl();
    }

    @Override
    public String getName() {
        return "compose_query_by_id";
    }

    @Override
    public String getDescription() {
        return "ComposeDeleteService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);

        try {
            UUIDVerify.userRightsVerification(userUniqueId ,"GET");
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
            exception.setMessage("id is empty");
            exception.setUserMessage("id is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            ComposeDto   composeDto= composeService.queryComposeById(userUniqueId,id);
            if(composeDto==null)
                return  new ComposeDto();
            return  composeDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("compose query fail");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("compose query fail:{" + e.getMessage() + "}");
            throw exception;
        }
    }
}
