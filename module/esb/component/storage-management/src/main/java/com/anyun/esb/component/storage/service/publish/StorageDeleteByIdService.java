package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.StorageDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.storage.common.UUIDVerify;
import com.anyun.esb.component.storage.dao.StorageDao;
import com.anyun.esb.component.storage.dao.impl.StorageDaoImpl;
import com.anyun.esb.component.storage.service.storage.StorageService;
import com.anyun.esb.component.storage.service.storage.impl.StorageServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gp on 17-4-13.
 */
public class StorageDeleteByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageDeleteByIdService.class);
    private StorageService storageService;
    private StorageDao storageDao;

    public StorageDeleteByIdService() {
        storageService = new StorageServiceImpl();
        storageDao = new StorageDaoImpl();
    }

    @Override
    public String getName() {
        return "storage_delete";
    }

    @Override
    public String getDescription() {
        return "Storage Delete";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
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
        LOGGER.debug("id:[{}]",id);
        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [id] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        StorageDto dto = storageDao.selectStorageById(id);
        if (dto == null || StringUtils.isEmpty(dto.getId())) {
            return new Status<String>("success");
        }
        try {
            storageService.deleteStorageById(id);
            return new Status<String>("success");
        }catch (Exception e){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("storage delete error [" + e.getMessage() + "]");
            exception.setUserMessage("存储删除失败[" + e.getMessage() + "]");
            exception.setUserTitle("存储删除失败");
            throw exception;
        }
    }
}
