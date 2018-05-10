package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.SecurityGroupService;
import com.anyun.esb.component.host.service.docker.impl.SecurityGroupServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by twh-workspace on 17-5-12.
 */
public class SecurityGroupQueryByConditionsService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityGroupQueryByConditionsService.class);
    private SecurityGroupService securityGroupService;

    public SecurityGroupQueryByConditionsService() {
        securityGroupService = new SecurityGroupServiceImpl();
    }

    @Override
    public String getName() {
        return "SecurityGroup_query_by_conditions";
    }

    @Override
    public String getDescription() {
        return "SecurityGroup Query By Conditions Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String s = exchange.getIn().getHeader("s",String.class);
        LOGGER.debug("s:[{}]",s);
        CommonQueryParam commonQueryParam = JsonUtil.fromJson(CommonQueryParam.class, s);
        try {
            UUIDVerify.userRightsVerification(commonQueryParam.getUserUniqueId(),"QUERY");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        try {
            return securityGroupService.queryByConditions(commonQueryParam);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("安全组查询失败" + e.getMessage());
            exception.setUserMessage("安全组查询失败" + e.getMessage());
            exception.setUserTitle("安全组查询失败");
            throw exception;
        }
    }
}
