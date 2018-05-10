package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
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

/**
 * Created by sxt on 16-10-18.
 */
public class SecurityGroupDeleteByLabelService extends AbstractBusinessService {
    private final  static Logger  LOGGER = LoggerFactory.getLogger(SecurityGroupDeleteByLabelService.class);
    private SecurityGroupService securityGroupService;

    public SecurityGroupDeleteByLabelService(){
          securityGroupService=new SecurityGroupServiceImpl();
    }

    @Override
    public String getName() {
        return "securityGroup_delete_by_label";
    }

    @Override
    public String getDescription() {
        return "SecurityGroupDeleteByLabelService";
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
           securityGroupService.securityGroupDeleteByLabel(label);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("SecurityGroupDeleteByLabel fail");
            exception.setUserMessage("SecurityGroupDeleteByLabel fail[" + exception + "]");
            exception.setUserTitle("SecurityGroupDeleteByLabel fail");
            throw exception;
        }
    }
}

