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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/4/10.
 */
public class NetL2ListQueryService extends AbstractBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetL2InfoQueryByIdService.class);
    private NetL2InfoService netL2InfoService;

    public NetL2ListQueryService() {
        netL2InfoService = new NetL2InfoServiceImpl();
    }

    @Override
    public String getName() {
        return "netL2Info_list_query_by_type";
    }

    @Override
    public String getDescription() {
        return "NetL2ListQueryService";
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

        String type = exchange.getIn().getHeader("type", String.class);
        LOGGER.debug("type:[{}]",type);
        if (StringUtils.isEmpty(type)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param [type] is empty");
            exception.setUserMessage("service param [type] is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            List<NetL2InfoDto> l = netL2InfoService.getNetL2InfoListByType(type);
            if (l == null)
                return new ArrayList<>();
            return l;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("NetL2Info  Query  error [" + e.getMessage() + "]");
            exception.setUserMessage("L2网络列表 查询错误  [" + e.getMessage() + "]");
            exception.setUserTitle("L2网络列表 查询错误");
            throw exception;
        }
    }
}
