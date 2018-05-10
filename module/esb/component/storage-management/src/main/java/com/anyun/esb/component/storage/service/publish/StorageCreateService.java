package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.StorageDto;
import com.anyun.cloud.param.StorageCreateParam;
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
 * Created by sxt on 16-8-10.
 */
public class StorageCreateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageCreateService.class);
    private StorageService storageService;
    private StorageDao storageDao;

    public StorageCreateService() {
        storageService = new StorageServiceImpl();
        storageDao = new StorageDaoImpl();
    }

    @Override
    public String getName() {
        return "storage_create";
    }

    @Override
    public String getDescription() {
        return "StorageCreateService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        LOGGER.debug("Camel context [{}]", endpoint.getCamelContext());
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        StorageCreateParam param = JsonUtil.fromJson(StorageCreateParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"PUT");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getPurpose())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [purpose] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (!param.getPurpose().matches("(docker.runtime)|(docker.volume)")) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [purpose] is  not  in(docker.runtime,docker.volume)");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getType())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [type] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (!param.getType().matches("(gluster)|(yeestore)|(nfs)")) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [type] is  not  in(gluster,yeestore,nfs)");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getFilesystem())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [filesystem] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            return  storageService.storageCreate(param);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("storage create error [" + e.getMessage() + "]");
            exception.setUserMessage("存储创建失败[" + e.getMessage() + "]");
            exception.setUserTitle("存储创建失败");
            throw exception;
        }
    }
}
