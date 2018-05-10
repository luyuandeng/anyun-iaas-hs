package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.NetLabelInfoDto;
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
 * Created by sxt on 16-7-25.
 */
public class NetLabelInfoQueryAllBridgeService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetLabelInfoQueryAllBridgeService.class);
    private NetService service;

    public NetLabelInfoQueryAllBridgeService() {
        service = new NetServiceImpl();
    }

    @Override
    public String getName() {
        return "netLabelInfo_query_all_bridge";
    }

    @Override
    public String getDescription() {
        return "NetLabelInfoQueryAllBridgeService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String id = exchange.getIn().getHeader("id", String.class);
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
        try {
            List<NetLabelInfoDto> l = service.netLabelInfoQueryAllBridge();
            if (l == null)
                return new ArrayList<NetLabelInfoDto>();
            return l;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("NetLabelInfo Query All bridge fail \n " + e.getMessage());
            exception.setUserMessage("查询网络信息[" + e.getMessage() + "]");
            exception.setUserTitle("查询网络信息失败");
            throw exception;
        }
    }
}
