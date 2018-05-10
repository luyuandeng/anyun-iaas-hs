package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.param.VolumeUpdateParam;
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
public class VolumeUpdateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VolumeUpdateService.class);
    private VolumeService volumeService;
    private VolumeDao volumeDao;
    private StorageDao storageDao;

    public VolumeUpdateService() {
        volumeService = new VolumeServiceImpl();
        volumeDao = new VolumeDaoImpl();
        storageDao = new StorageDaoImpl();
    }

    @Override
    public String getName() {
        return "volume_update";
    }

    @Override
    public String getDescription() {
        return "VolumeUpdateService";
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

        VolumeUpdateParam param = JsonUtil.fromJson(VolumeUpdateParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Param is empty");
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

        VolumeDto volumeDto = volumeDao.selectVolumeById(param.getId());
        if (volumeDto == null || StringUtils.isEmpty(volumeDto.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("volume does not exist");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        boolean mark = true; //是否修改卷的大小
        if (param.getSpace() == volumeDto.getSpace())
            mark = false;
        else {
            if (param.getSpace() <= 0) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("Param [space:" + param.getSpace() + "]<= 0");
                exception.setUserMessage("服务参数格式化错误[" + exception + "]");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }
            if (param.getSpace() < volumeDto.getSpace()) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("param[space:" + param.getSpace() + "] < volumeDto.getSpace()[space:" + volumeDto.getSpace() + "]");
                exception.setUserMessage("服务参数格式化错误[" + exception + "]");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }

            ProjectDto projectDto = storageDao.selectProjectById(volumeDto.getProject());
            if (param.getSpace() > projectDto.getSpace()) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("Project space is insufficient \n   project:" + projectDto.getSpace() + " \t  volume:" + param.getSpace());
                exception.setUserMessage("服务参数格式化错误[" + exception + "]");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }

            int allVolumeSpace = 0;
            List<VolumeDto> volumeList = volumeDao.selectVolumeByProject(volumeDto.getProject());
            if (volumeList != null && !volumeList.isEmpty()) {
                for (VolumeDto dto : volumeList) {
                    if (dto == null) {
                        continue;
                    }
                    allVolumeSpace += dto.getSpace();
                }
            }
            allVolumeSpace += param.getSpace();
            if (allVolumeSpace > projectDto.getSpace()) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("Project space is insufficient \n  project:" + projectDto.getSpace() + " \t  allVolumeSpace：" + allVolumeSpace);
                exception.setUserMessage("服务参数格式化错误[" + exception + "]");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }
        }

        try {
            LOGGER.debug("mark:[{}]", mark);
            return  volumeService.volumeUpdate(param, mark);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("volume update error [" + e.getMessage() + "]");
            exception.setUserMessage("卷修改失败[" + e.getMessage() + "]");
            exception.setUserTitle("卷修改失败");
            throw exception;
        }
    }
}
