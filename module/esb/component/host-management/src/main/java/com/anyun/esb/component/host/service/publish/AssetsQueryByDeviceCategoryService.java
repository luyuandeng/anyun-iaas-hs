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

import java.util.ArrayList;
import java.util.List;

public class AssetsQueryByDeviceCategoryService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AssetsQueryByDeviceCategoryService.class);
    private AssetsService assetsService;

    public AssetsQueryByDeviceCategoryService() {
        assetsService = new AssetsServiceImpl();
    }

    @Override
    public String getName() {
        return "assets_list_query_by_deviceCategory";
    }

    @Override
    public String getDescription() {
        return null;
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
        String DeviceCategory = exchange.getIn().getHeader("DeviceCategory", String.class);
        LOGGER.debug("DeviceCategory:[{}]", DeviceCategory);
        if (StringUtils.isEmpty(DeviceCategory)) {
            LOGGER.debug("Assets DeviceCategory is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Assets DeviceCategory is empty");
            exception.setUserMessage("Assets DeviceCategory is  empty");
            exception.setUserTitle("Assets param error");
            throw exception;
        }
        try {
            List<AssetsDto> list = assetsService.QueryAssetsBydeviceCategory(DeviceCategory);
            if (list == null)
                list = new ArrayList<>();
            return list;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("资产管理查询失败");
            throw exception;
        }
    }
}
