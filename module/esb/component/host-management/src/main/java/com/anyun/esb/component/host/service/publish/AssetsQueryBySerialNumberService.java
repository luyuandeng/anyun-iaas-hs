package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.AssetsService;
import com.anyun.esb.component.host.service.docker.impl.AssetsServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by twh-workspace on 17-4-10.
 */
public class AssetsQueryBySerialNumberService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetsQueryBySerialNumberService.class);
    private AssetsService assetsService;

    public AssetsQueryBySerialNumberService() {
        assetsService = new AssetsServiceImpl();
    }

    @Override
    public String getName() {
        return "assets_base_query_by_serialNumber";
    }

    @Override
    public String getDescription() {
        return "AssetsQueryBySerialNumberService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId= exchange.getIn().getHeader("userUniqueId",String.class);
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

        String serialNumber= exchange.getIn().getHeader("serialNumber",String.class);
        LOGGER.debug("serialNumber:[{}]", serialNumber);
        if (StringUtils.isEmpty(serialNumber)) {
            LOGGER.debug("Assets SerialNumber is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Assets SerialNumber is empty");
            exception.setUserMessage("Assets SerialNumber is  empty");
            exception.setUserTitle("Assets param error");
            throw exception;
        }

        try {
            AssetsDto dto =assetsService.queryBysSerialNumber(serialNumber);
            if (dto == null)
                return new AssetsDto();
            return dto;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("根据 序列号 查询资产 失败");
            throw exception;
        }
    }
}
