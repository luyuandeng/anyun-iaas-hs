package com.anyun.esb.component.host.service.publish;


import com.anyun.cloud.dto.MonitorOverviewDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;

import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.OverviewService;
import com.anyun.esb.component.host.service.docker.impl.OverviewServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Created by gp on 16-9-12.
 */
public class MonitorOverviewService extends AbstractBusinessService{
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorOverviewService.class);
    private OverviewService overviewService;

    public MonitorOverviewService() {
        overviewService=new OverviewServiceImpl();
    }

    @Override
    public String getName() {
        return "query_overview_monitor";
    }

    @Override
    public String getDescription() {
        return "Query Overview Monitor";
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
            MonitorOverviewDto dto =overviewService.queryOverviewMonitor(userUniqueId);

            return dto;
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
