package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.NetL2InfoDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.NetL2InfoService;
import com.anyun.esb.component.host.service.docker.impl.NetL2InfoServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by sxt on 17-4-10.
 */
public class NetL2InfoQueryByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetL2InfoQueryByIdService.class);
    private NetL2InfoService netL2InfoService;

    public NetL2InfoQueryByIdService() {
        netL2InfoService = new NetL2InfoServiceImpl();
    }

    @Override
    public String getName() {
        return "netL2Info_query_by_id";
    }

    @Override
    public String getDescription() {
        return "NetL2InfoQueryByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "GET");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String id = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("id:[{}]",id);
        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param [id] is empty");
            exception.setUserMessage("service param [id] is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            NetL2InfoDto netL2InfoDto = netL2InfoService.queryNetL2InfoById(id);
            if (netL2InfoDto == null)
                return new NetL2InfoDto();
            return netL2InfoDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("NetL2Info  Query  error [" + e.getMessage() + "]");
            exception.setUserMessage("L2网络 查询错误  [" + e.getMessage() + "]");
            exception.setUserTitle("L2网络 查询错误");
            throw exception;
        }
    }
}
