package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.MonitorContainerDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.MonitorService;
import com.anyun.esb.component.host.service.docker.impl.MonitorServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gp on 16-9-23.
 */
public class MonitorQueryContainerByHostService extends AbstractBusinessService{
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorQueryContainerByHostService.class);
    private MonitorService monitorService;

    public MonitorQueryContainerByHostService(){
        monitorService = new MonitorServiceImpl();
    }

    @Override
    public String getName() {
        return "query_container_by_host";
    }

    @Override
    public String getDescription() {
        return "Monitor Query Container By Host";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        try{
            String host = exchange.getIn().getHeader("hostid",String.class);
            String userUniqueId = exchange.getIn().getHeader("userUniqueId",String.class);

            try {
                UUIDVerify.userRightsVerification(userUniqueId,"GET");
            } catch (Exception  e) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setUserTitle("User rights validation failed");
                exception.setUserMessage(e.getMessage());
                exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
                throw exception;
            }
            LOGGER.debug("宿主机ID ："+host);
            if (StringUtils.isEmpty(host)) {
                LOGGER.debug("host is empty");
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("host is empty");
                exception.setUserMessage("host is  empty");
                exception.setUserTitle("host param error");
                throw exception;
            }
            MonitorContainerDto dto = monitorService.queryContainerByHost(host);
            return dto;
        }catch (Exception e){
            LOGGER.debug("Exception is:[{}]", e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("query Container By Host error");
            exception.setUserMessage("Container By Host Isempty");
            exception.setMessage(e.getMessage());
            throw exception;
        }
    }
}
