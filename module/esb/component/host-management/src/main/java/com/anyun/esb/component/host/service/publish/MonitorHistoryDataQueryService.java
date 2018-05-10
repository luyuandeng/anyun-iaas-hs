package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.MonitorHostDto;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 17-6-15.
 */
public class MonitorHistoryDataQueryService  extends AbstractBusinessService{
    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorHistoryDataQueryService.class);
    private MonitorService monitorService;
    public MonitorHistoryDataQueryService(){
        monitorService=new MonitorServiceImpl();
    }
    @Override
    public String getName() {
        return "query_monitor_history_data";
    }

    @Override
    public String getDescription() {
        return "MonitorHistoryDataQueryService";
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

        String subParameters = exchange.getIn().getHeader("subParameters", String.class);
        LOGGER.debug("subParameters:[{}]", subParameters);
        if (StringUtils.isEmpty(subParameters)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("subParameters is empty");
            exception.setUserMessage("subParameters is  empty");
            exception.setUserTitle("subParameters param error");
            throw exception;
        }
        try {
            List<MonitorHostDto> dto = monitorService.getMonitorHistoryData(subMethod, subParameters);
            if (dto == null)
                return new ArrayList<MonitorHostDto>();
            return dto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Monitor history data  Query   fail: [" + e.getMessage() + "]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("监控 历史 数据 查询 失败");
            throw exception;
        }
    }
}
