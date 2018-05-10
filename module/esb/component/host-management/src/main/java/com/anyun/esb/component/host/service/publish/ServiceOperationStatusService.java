package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ServiceOperationDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.ServiceOperationService;
import com.anyun.esb.component.host.service.docker.impl.ServiceOperationServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by gp on 16-12-14.
 */
public class ServiceOperationStatusService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceOperationStatusService.class);
    private ServiceOperationService serviceOperationService;

    public ServiceOperationStatusService(){serviceOperationService=new ServiceOperationServiceImpl();}

    @Override
    public String getName() {
        return "query_service_operation_status";
    }

    @Override
    public String getDescription() {
        return "Query Service Operation Status ";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId",String.class);
        LOGGER.debug("用户权限id："+userUniqueId);

        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        try{
            List<ServiceOperationDto> list = serviceOperationService.queryServiceOperationStatus();
            return list;
        }catch (Exception e){
            LOGGER.debug("Exception is:[{}]", e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("query Overview Monitor error");
            exception.setUserMessage("query Overview Monitor error");
            exception.setMessage(e.getMessage());
            throw exception;
        }

    }
}
