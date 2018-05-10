package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.service.docker.DiskSchemeService;
import com.anyun.esb.component.host.service.docker.impl.DiskSchemeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DiskSchemeDeleteByIdService extends AbstractBusinessService {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DiskSchemeDeleteByIdService.class);
    private DiskSchemeService diskSchemeService;

    public DiskSchemeDeleteByIdService() {
        diskSchemeService = new DiskSchemeServiceImpl();
    }


    @Override
    public String getName() {
        return "diskScheme_delete_by_id";
    }

    @Override
    public String getDescription() {
        return "diskScheme Delete By Id";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "DELETE");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        String id = exchange.getIn().getHeader("id", String.class);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("is Id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Id is empty");
            exception.setUserMessage("Id is  empty");
            exception.setUserTitle("Id is  empty");
            throw exception;
        }


        try {
            DiskSchemeDto diskSchemeDto = diskSchemeService.queryDiskSchemeByid(id);
            if (diskSchemeDto != null) {
                diskSchemeService.deleteDiskSchemeById(id);
                return new Status<String>("success");
            } else
                return new Status<String>("false");
        } catch (Exception e) {
            LOGGER.debug("Exception:[{}]", e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("删除磁盘方案错误 [" + e + "]");
            exception.setUserMessage("删除磁盘方案失败[" + e + "]");
            exception.setUserTitle("删除磁盘方案失败");
            throw exception;
        }
    }
}
