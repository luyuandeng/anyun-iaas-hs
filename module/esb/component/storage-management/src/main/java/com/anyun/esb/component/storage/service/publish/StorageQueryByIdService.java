package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.dto.StorageDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.storage.common.UUIDVerify;
import com.anyun.esb.component.storage.service.storage.StorageService;
import com.anyun.esb.component.storage.service.storage.impl.StorageServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-8-12.
 */
public class StorageQueryByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageQueryByIdService.class);
    private StorageService storageService;

    public StorageQueryByIdService() {
        storageService = new StorageServiceImpl();
    }

    @Override
    public String getName() {
        return "storage_query_by_id";
    }

    @Override
    public String getDescription() {
        return "StorageQueryByIdService";
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

        String id = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [id] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            StorageDto storageDto = storageService.storageQueryById(id);
            if (storageDto == null || StringUtils.isEmpty(storageDto.getId()))
                return new StorageDto();
            return storageDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("storage query error [" + e.getMessage() + "]");
            exception.setUserMessage("存储 查询失败[" + e.getMessage() + "]");
            exception.setUserTitle("存储 查询失败");
            throw exception;
        }
    }
}
