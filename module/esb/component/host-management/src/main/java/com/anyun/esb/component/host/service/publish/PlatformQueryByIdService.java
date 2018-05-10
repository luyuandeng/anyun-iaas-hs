package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.PlatformDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.PlatformService;
import com.anyun.esb.component.host.service.docker.impl.PlatformServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-10-13.
 */
public class PlatformQueryByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformQueryByIdService.class);
    private PlatformService platformService;

    public PlatformQueryByIdService() {
        platformService = new PlatformServiceImpl();
    }

    @Override
    public String getName() {
        return "platform_query_by_id";
    }

    @Override
    public String getDescription() {
        return "PlatformQueryByIdService";
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
        String id = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("服务参数格式化错误[param[id] is empty]");
            exception.setUserMessage("Service param format error");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            PlatformDto platformDto = platformService.platformQueryById(id);
            if (platformDto == null)
                return new PlatformDto();
            return platformDto;
        } catch (Exception e) {
            e.printStackTrace();
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("Platform query by id  fail");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("platform query by id is fail:{" + e.getMessage() + "}");
            throw exception;
        }
    }
}
