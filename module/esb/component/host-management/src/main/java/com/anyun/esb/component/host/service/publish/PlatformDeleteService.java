package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PlatformDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.PlatformDao;
import com.anyun.esb.component.host.dao.impl.PlatformDaoImpl;
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
public class PlatformDeleteService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformDeleteService.class);
    public PlatformService platformService;
    public PlatformDao platformDao;

    public PlatformDeleteService() {
        platformService = new PlatformServiceImpl();
        platformDao = new PlatformDaoImpl();
    }

    @Override
    public String getName() {
        return "platform_delete";
    }

    @Override
    public String getDescription() {
        return "PlatformDeleteService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId=exchange.getIn().getHeader("userUniqueId",String.class);
        LOGGER.debug("userUniqueId:[{}]",userUniqueId);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"DELETE");
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
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("Parameter check error");
            exception.setUserMessage("id is empty");
            exception.setMessage("id is empty");
            throw exception;
        }
        PlatformDto platformDto = platformDao.selectPlatformById(id);
        if (platformDto == null || StringUtils.isEmpty(platformDto.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("Parameter check error");
            exception.setUserMessage("The param id does exists");
            exception.setMessage("The param id does exists");
            throw exception;
        }

        try {
            platformService.platformDelete(id);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("Platform delete fail");
            exception.setUserMessage("Platform delete fail" + e.getMessage());
            exception.setMessage("Platform delete fail:" + e.getMessage());
            throw exception;
        }

    }
}
