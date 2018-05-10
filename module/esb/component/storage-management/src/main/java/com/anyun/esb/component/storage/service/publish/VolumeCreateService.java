package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.param.VolumeCreateParam;
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

import java.util.List;

/**
 * Created by sxt on 16-8-11.
 */
public class VolumeCreateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VolumeCreateService.class);
    private VolumeService volumeService;
    private VolumeDao dao;
    private StorageDao storageDao;

    public VolumeCreateService() {
        volumeService = new VolumeServiceImpl();
        dao = new VolumeDaoImpl();
        storageDao = new StorageDaoImpl();
    }

    @Override
    public String getName() {
        return "volume_create";
    }

    @Override
    public String getDescription() {
        return "VolumeCreateService";
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
        VolumeCreateParam param = JsonUtil.fromJson(VolumeCreateParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
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


        if (StringUtils.isEmpty(param.getProject())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [project] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        ProjectDto projectDto = storageDao.selectProjectById(param.getProject());
        if (projectDto == null || StringUtils.isEmpty(projectDto.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("project does not exist");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (param.getSpace() <= 0) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [space] <= 0  ");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        int allVolumeSpace = 0;
        if (param.getSpace() > projectDto.getSpace()) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Project space is insufficient \n project:" + projectDto.getSpace() + "\t volume:" + param.getSpace());
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getStorageId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("StorageId space is null");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        List<VolumeDto> volumeList = dao.selectVolumeByProject(param.getProject());
        if (volumeList != null && !volumeList.isEmpty()) {
            for (VolumeDto volumeDto : volumeList) {
                if (volumeDto == null || StringUtils.isEmpty(volumeDto.getId())) {
                    continue;
                }
                allVolumeSpace += volumeDto.getSpace();
            }
        }

        allVolumeSpace += param.getSpace();
        if (allVolumeSpace > projectDto.getSpace()) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Project space is insufficient \n project:" + projectDto.getSpace() + "\t allVolumeSpace:" + allVolumeSpace);
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            return  volumeService.volumeCreate(param);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("volume create error [" + e.getMessage() + "]");
            exception.setUserMessage("卷创建失败[" + e.getMessage() + "]");
            exception.setUserTitle("卷创建失败");
            throw exception;
        }
    }
}
