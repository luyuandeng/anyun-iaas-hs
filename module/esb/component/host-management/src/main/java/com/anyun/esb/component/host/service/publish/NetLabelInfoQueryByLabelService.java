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

/**
 * Created by sxt on 16-7-26.
 */
public class NetLabelInfoQueryByLabelService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetLabelInfoQueryByLabelService.class);
    private NetService service;

    public NetLabelInfoQueryByLabelService() {
        service = new NetServiceImpl();
    }

    @Override
    public String getName() {
        return "netLabelInfo_query_by_label";
    }

    @Override
    public String getDescription() {
        return "NetLabelInfoQueryByLabelService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
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

        String label = exchange.getIn().getHeader("label", String.class);
        LOGGER.debug("label:[{}]", label);
        if (StringUtils.isEmpty(label)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param [label] is empty");
            exception.setUserMessage("service param [label] is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            NetLabelInfoDto netLabelInfoDto = service.netLabelInfoQueryByLabel(label);
            if (netLabelInfoDto == null)
                return new NetLabelInfoDto();
            return netLabelInfoDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("NetLabelInfo  Query  error [" + e.getMessage() + "]");
            exception.setUserMessage("网络标签信息查询错误  [" + e.getMessage() + "]");
            exception.setUserTitle("网络标签信息查询错误");
            throw exception;
        }
    }
}
