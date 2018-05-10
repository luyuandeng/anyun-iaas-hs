package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.param.StorageUpdateParam;
import com.anyun.cloud.param.StorageUpdateStateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
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
 * Created by sxt on 16-8-11.
 */
public class StorageUpdateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageUpdateService.class);
    private StorageService storageService;
    private StorageDao storageDao;

    public StorageUpdateService() {
        storageService = new StorageServiceImpl();
        storageDao = new StorageDaoImpl();
    }

    @Override
    public String getName() {
        return "storage_update";
    }

    @Override
    public String getDescription() {
        return "StorageUpdateService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;

        }
        StorageUpdateParam param = JsonUtil.fromJson(StorageUpdateParam.class, postBody);
        LOGGER.debug("param[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }


        if (StringUtils.isEmpty(param.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [id] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            return storageService.storageUpdate(param);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("storage update error [" + e.getMessage() + "]");
            exception.setUserMessage("存储修改失败[" + e.getMessage() + "]");
            exception.setUserTitle("存储修改失败");
            throw exception;
        }
    }
}
