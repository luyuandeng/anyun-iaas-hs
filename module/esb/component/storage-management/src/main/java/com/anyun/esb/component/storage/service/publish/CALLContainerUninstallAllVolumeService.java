package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.ContainerUninstallVolumeParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.storage.service.storage.StorageService;
import com.anyun.esb.component.storage.service.storage.VolumeService;
import com.anyun.esb.component.storage.service.storage.impl.StorageServiceImpl;
import com.anyun.esb.component.storage.service.storage.impl.VolumeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-8-22.
 */
public class CALLContainerUninstallAllVolumeService  extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CALLContainerUninstallAllVolumeService.class);
    private VolumeService volumeService;
    public CALLContainerUninstallAllVolumeService() {
        volumeService = new VolumeServiceImpl();
    }

    @Override
    public String getName() {
        return "container_uninstall_all_volume_by_container";
    }

    @Override
    public String getDescription() {
        return "ContainerUninstallAllVolumeByContainerService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]",postBody);
        if (StringUtils.isEmpty(postBody)) {
            LOGGER.debug("postBody is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        Id<String>   param = JsonUtil.fromJson(Id.class, postBody);
        if (param == null) {
            LOGGER.debug("param is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getId())) {
            LOGGER.debug("param [id] is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [id] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            LOGGER.debug("[param] is:{}", JsonUtil.toJson(param));
            volumeService.containerUninstallAllVolumeByContainer(param.getId());
            return new Status<String>("success");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("exception is:{}", e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Container uninstall volume  error [" + e.getMessage() + "]");
            exception.setUserMessage("容器卸载卷失败[" + e.getMessage() + "]");
            exception.setUserTitle("容器卸载卷失败");
            throw exception;
        }
    }
}
