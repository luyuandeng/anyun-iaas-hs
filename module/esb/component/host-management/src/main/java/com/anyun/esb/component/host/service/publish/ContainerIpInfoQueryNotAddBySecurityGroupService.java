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
 * Created by sxt on 16-10-24.
 */
public class ContainerIpInfoQueryNotAddBySecurityGroupService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerIpInfoQueryNotAddBySecurityGroupService.class);
    private NetService netService;

    public ContainerIpInfoQueryNotAddBySecurityGroupService() {
        netService = new NetServiceImpl();
    }


    @Override
    public String getName() {
        return "containerIpInfo_query_notAdd_by_securityGroup";
    }

    @Override
    public String getDescription() {
        return "ContainerIpInfoQueryNotAddBySecurityGroupService";
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
        try {
            List<ContainerIpInfoDto> list = netService.containerIpInfoQueryNotAddBySecurityGroup(label);
            if (list == null)
                return new ArrayList<ContainerIpInfoDto>();
            return list;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("ContainerIpInfoQueryNotAddBySecurityGroup fail [" + e.getMessage() + "]");
            exception.setUserMessage("ContainerIpInfoQueryNotAddBySecurityGroup fail [" + e.getMessage() + "]");
            exception.setUserTitle("ContainerIpInfoQueryNotAddBySecurityGroup fail");
            throw exception;
        }
    }
}
