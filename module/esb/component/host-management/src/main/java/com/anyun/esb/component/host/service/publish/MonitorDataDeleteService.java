package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
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
 * Created by sxt on 17-6-15.
 */
public class MonitorDataDeleteService  extends AbstractBusinessService{
    private static final Logger LOGGER= LoggerFactory.getLogger(MonitorDataDeleteService.class);
    private MonitorService monitorService;

    public MonitorDataDeleteService(){
        monitorService=new MonitorServiceImpl();
    }

    @Override
    public String getName() {
        return "delete_monitor_data";
    }

    @Override
    public String getDescription() {
        return "MonitorDataDeleteService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId=exchange.getIn().getHeader("userUniqueId",String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"DELETE");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        String endTime = exchange.getIn().getHeader("endTime", String.class);
        LOGGER.debug("endTime:",endTime);
        if (StringUtils.isEmpty(endTime)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("endTime check error");
            exception.setUserMessage("endTime is empty");
            exception.setMessage("endTime is empty");
            throw exception;
        }

        try {
            monitorService.deleteMonitorData(endTime);
            return new Status<Boolean>(true);
        } catch (Exception e){
            LOGGER.debug(e.getMessage());
            return new Status<Boolean>(false);
        }
    }
}
