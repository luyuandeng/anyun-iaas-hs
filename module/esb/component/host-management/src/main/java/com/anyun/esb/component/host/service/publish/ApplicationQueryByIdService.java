package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ApplicationInfoDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.ApplicationService;
import com.anyun.esb.component.host.service.docker.impl.ApplicationServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-10-25.
 */
public class ApplicationQueryByIdService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationQueryByIdService.class);
    private ApplicationService applicationService;

    public ApplicationQueryByIdService() {
        applicationService = new ApplicationServiceImpl();
    }

    @Override
    public String getName() {
        return "application_query_by_id";
    }

    @Override
    public String getDescription() {
        return "ApplicationQueryByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);

        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String id = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            ApplicationInfoDto dto = applicationService.applicationQueryById(id);
            if (dto == null)
                return new ApplicationInfoDto();
            return dto;
        } catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("applicationQueryById  error [" + ex.getMessage() + "]");
            exception.setUserMessage("应用查询失败[" + ex.getMessage() + "]");
            exception.setUserTitle("应用查询失败");
            throw exception;
        }
    }
}
