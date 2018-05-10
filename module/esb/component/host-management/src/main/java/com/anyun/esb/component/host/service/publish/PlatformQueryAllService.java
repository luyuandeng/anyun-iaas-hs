package com.anyun.esb.component.host.service.publish;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-10-13.
 */
public class PlatformQueryAllService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformQueryAllService.class);
    private PlatformService platformService;

    public PlatformQueryAllService() {
        platformService = new PlatformServiceImpl();
    }

    @Override
    public String getName() {
        return "platform_query_all";
    }

    @Override
    public String getDescription() {
        return "PlatformQueryAllService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId=exchange.getIn().getHeader("userUniqueId",String.class);
        LOGGER.debug("userUniqueId:[{}]",userUniqueId);
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
            List<PlatformDto> list = platformService.platformQueryAll();
            if (list == null)
                return new ArrayList<PlatformDto>();
            return list;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("platform query fail");
            exception.setUserMessage("platform query fail" + e.getMessage());
            exception.setMessage("platform query fail:{" + e.getMessage() + "}");
            throw exception;
        }
    }
}
