package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ContainerDto;
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

import java.util.List;

/**
 * Created by gp on 16-11-25.
 */
public class MonitorQueryContainerIfoByHostIdService extends AbstractBusinessService{
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorQueryContainerIfoByHostIdService.class);
    private MonitorService monitorService;

    public MonitorQueryContainerIfoByHostIdService(){
        monitorService = new MonitorServiceImpl();
    }
    @Override
    public String getName() {
        return "query_containerInfp_by_hostId";
    }

    @Override
    public String getDescription() {
        return "Query Container Info By HostId";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        try{
            String hostid = exchange.getIn().getHeader("hostid",String.class);
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

            LOGGER.debug("宿主机ID ："+hostid);
            if (StringUtils.isEmpty(hostid)) {
                LOGGER.debug("host is empty");
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("host is empty");
                exception.setUserMessage("host is  empty");
                exception.setUserTitle("host param error");
                throw exception;
            }
            List<ContainerDto> list = monitorService.queryContainerInfoByHostId(hostid);
            return list;
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
