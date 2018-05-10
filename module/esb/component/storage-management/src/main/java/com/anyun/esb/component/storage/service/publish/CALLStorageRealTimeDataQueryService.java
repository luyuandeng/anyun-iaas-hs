package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.dto.StorageInfo;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.storage.service.storage.StorageService;
import com.anyun.esb.component.storage.service.storage.impl.StorageServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-10-19.
 */
public class CALLStorageRealTimeDataQueryService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CALLStorageRealTimeDataQueryService.class);
    private StorageService storageService;

    public CALLStorageRealTimeDataQueryService() {
        storageService = new StorageServiceImpl();
    }

    @Override
    public String getName() {
        return "storage_realTimeData_query";
    }

    @Override
    public String getDescription() {
        return "StorageRealTimeDataQueryService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        try {
            StorageInfo storageInfo = storageService.storageRealTimeDataQuery();
            if (storageInfo == null)
                return new StorageInfo();
            else {
                LOGGER.debug("storageInfo:[{}]", storageInfo.asJson());
                return storageInfo;
            }
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("StorageRealTimeDataQuery fail [" + e.getMessage() + "]");
            exception.setUserMessage("StorageRealTimeDataQuery fail[" + e.getMessage() + "]");
            exception.setUserTitle("StorageRealTimeDataQuery fail");
            throw exception;
        }
    }
}
