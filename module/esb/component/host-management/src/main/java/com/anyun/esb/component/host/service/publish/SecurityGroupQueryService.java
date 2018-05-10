package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.SecurityGroupDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.SecurityGroupService;
import com.anyun.esb.component.host.service.docker.impl.SecurityGroupServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twh-workspace on 17-3-14.
 */
public class SecurityGroupQueryService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityGroupQueryService.class);
    private SecurityGroupService securityGroupService;

    public SecurityGroupQueryService() {
        securityGroupService = new SecurityGroupServiceImpl();
    }

    @Override
    public String getName() {
        return "securityGroup_list_query";
    }

    @Override
    public String getDescription() {
        return "SecurityGroupQueryService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "GET");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String subMethod = exchange.getIn().getHeader("subMethod", String.class);
        LOGGER.debug("subMethod:[{}]", subMethod);
        if (StringUtils.isEmpty(subMethod)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("subMethod is empty");
            exception.setUserMessage("subMethod is  empty");
            exception.setUserTitle("subMethod param error");
            throw exception;
        }

        String subParameters = exchange.getIn().getHeader("subParameters",String.class);
        LOGGER.debug("subParameters:[{}]",subParameters);
        if(StringUtils.isEmpty(subParameters)){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("subParameters is empty");
            exception.setUserMessage("subParameters is  empty");
            exception.setUserTitle("subParameters param error");
            throw exception;
        }

        try {
            List<SecurityGroupDto> l = securityGroupService.securityGroupQuery(userUniqueId, subMethod,subParameters);
            if(l==null ||l.isEmpty())
                return  new ArrayList<>();
            return l;
        }
        catch (Exception e){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("SecurityGroup query fail: ["+e.getMessage()+"]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("查询安全组 失败");
            throw exception;
        }
    }
}
