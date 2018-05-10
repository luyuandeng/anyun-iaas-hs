package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ContainerIpInfoDto;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-10-21.
 */
public class ContainerIpInfoQueryBySecurityGroupService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerIpInfoQueryBySecurityGroupService.class);
    private NetService netService;

    public ContainerIpInfoQueryBySecurityGroupService() {
        netService = new NetServiceImpl();
    }

    @Override
    public String getName() {
        return "containerIpInfo_query_by_securityGroup";
    }

    @Override
    public String getDescription() {
        return "ContainerIpInfoQueryBySecurityGroupService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
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
            exception.setMessage("label is empty");
            exception.setUserMessage("label is empty");
            exception.setUserTitle("label is empty");
            throw exception;
        }
        try {
            List<ContainerIpInfoDto> list = netService.containerIpInfoQueryBySecurityGroup(label);
            if (list == null)
                list = new ArrayList<>();
            return list;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("ContainerIpInfoQueryBySecurityGroup fail");
            exception.setUserMessage("ContainerIpInfoQueryBySecurityGroup  fail[" + exception + "]");
            exception.setUserTitle("ContainerIpInfoQueryBySecurityGroup fail fail");
            throw exception;
        }
    }
}

