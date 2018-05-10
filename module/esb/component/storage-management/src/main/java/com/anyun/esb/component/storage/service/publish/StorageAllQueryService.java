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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-8-11.
 */
public class StorageAllQueryService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageAllQueryService.class);
    private StorageService storageService;

    public StorageAllQueryService() {
        storageService = new StorageServiceImpl();
    }

    @Override
    public String getName() {
        return "storage_all_query";
    }

    @Override
    public String getDescription() {
        return "StorageAllQueryService";
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
        try {
            List<StorageDto> storageDtoList = storageService.storageAllQuery();
            if (storageDtoList == null)
                return new ArrayList<StorageDto>();
            return storageDtoList;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("storage query error [" + e.getMessage() + "]");
            exception.setUserMessage("存储查询失败[" + e.getMessage() + "]");
            exception.setUserTitle("存储查询失败");
            throw exception;
        }
    }
}
