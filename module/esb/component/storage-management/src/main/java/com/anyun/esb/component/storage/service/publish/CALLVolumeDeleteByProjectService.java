package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.storage.dao.StorageDao;
import com.anyun.esb.component.storage.dao.impl.StorageDaoImpl;
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
public class CALLVolumeDeleteByProjectService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CALLVolumeDeleteByProjectService.class);
    private VolumeService   volumeService;
    private StorageDao storageDao;

    public CALLVolumeDeleteByProjectService() {
        volumeService = new VolumeServiceImpl();
        storageDao = new StorageDaoImpl();
    }

    @Override
    public String getName() {
        return "volume_delete_by_project";
    }

    @Override
    public String getDescription() {
        return "VolumeDeleteByProjectService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String id = exchange.getIn().getHeader("id", String.class);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("param [id] is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [id] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        ProjectDto projectDto = storageDao.selectProjectById(id);
        if (projectDto == null) {
            LOGGER.debug("project is not exist");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("project is not exist ");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            LOGGER.debug("[id] is:{}", id);
            volumeService.volumeDeleteByProject(id);
            return new Status<String>("success");
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("Exception is{}", e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("volume delete error [" + e.getMessage() + "]");
            exception.setUserMessage("卷 删除失败[" + e.getMessage() + "]");
            exception.setUserTitle("卷删除失败");
            throw exception;
        }
    }
}

