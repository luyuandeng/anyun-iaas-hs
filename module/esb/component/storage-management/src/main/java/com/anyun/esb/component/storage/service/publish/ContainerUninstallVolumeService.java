package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.param.ContainerUninstallVolumeParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.storage.common.UUIDVerify;
import com.anyun.esb.component.storage.dao.StorageDao;
import com.anyun.esb.component.storage.dao.VolumeDao;
import com.anyun.esb.component.storage.dao.impl.StorageDaoImpl;
import com.anyun.esb.component.storage.dao.impl.VolumeDaoImpl;
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
 * Created by sxt on 16-8-11.
 */
public class ContainerUninstallVolumeService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerUninstallVolumeService.class);
    private VolumeService volumeService;
    private VolumeDao volumeDao;
    private StorageDao storageDao;

    public ContainerUninstallVolumeService() {
        volumeService = new VolumeServiceImpl();
        volumeDao = new VolumeDaoImpl();
        storageDao = new StorageDaoImpl();
    }

    @Override
    public String getName() {
        return "container_uninstall_volume";
    }

    @Override
    public String getDescription() {
        return "ContainerUninstallVolumeService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        if (StringUtils.isEmpty(postBody)) {
            LOGGER.debug("postBody is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        ContainerUninstallVolumeParam param = JsonUtil.fromJson(ContainerUninstallVolumeParam.class, postBody);
        if (param == null) {
            LOGGER.debug("param is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
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

        if (StringUtils.isEmpty(param.getContainer())) {
            LOGGER.debug("param [container] is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [container] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        ContainerDto containerDto = storageDao.selectContainerById(param.getContainer());
        if (containerDto == null) {
            LOGGER.debug("[containerDto:{}] is not exit", param.getContainer());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("container is not exit");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getVolume())) {
            LOGGER.debug("param [volume] is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [volume] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }


        VolumeDto volumeDto = volumeDao.selectVolumeById(param.getVolume());
        if (volumeDto == null) {
            LOGGER.debug("[volume:{}] is not exit", param.getVolume());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("volume is not exit");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (!containerDto.getProjectId().equals(volumeDto.getProject())) {
            LOGGER.debug("volume:{} and  container：{} is not in same project", param.getVolume(), param.getContainer());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("volume and  container not in same project");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            LOGGER.debug("[param] is:{}", JsonUtil.toJson(param));
            volumeService.containerUninstallVolume(param);
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