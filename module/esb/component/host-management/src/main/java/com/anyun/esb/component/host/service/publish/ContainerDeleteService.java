package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-6-13.
 */
public class ContainerDeleteService extends AbstractBusinessService {
    private ContainerService containerService;
    private ContainerDao containerDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerDeleteService.class);

    public ContainerDeleteService() {
        containerService = new ContainerServiceImpl();
        containerDao = new ContainerDaoImpl();
    }

    @Override
    public String getName() {
        return "container_delete_by_id";
    }

    @Override
    public String getDescription() {
        return "container.delete.by_id";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId=exchange.getIn().getHeader("userUniqueId",String.class);
        LOGGER.debug("userUniqueId:[{}]",userUniqueId);

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
            LOGGER.debug("id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param [id] not exist");
            exception.setUserMessage("服务参数 [id] 不存在");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        ContainerDto containerDto = containerDao.selectContainerById(id);
        if (containerDto == null) {
            LOGGER.debug("containerDto is null");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("containerDto is null");
            exception.setUserMessage("containerDto is null");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            LOGGER.debug("param:[{}]", id);
            containerService.deleteContainer(id);
            return new Status<String>("success");
        } catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("container  delete  error  info[" + ex.getMessage() + "]");
            exception.setUserMessage("容器删除错误信息 [" + ex.getMessage() + "]");
            exception.setUserTitle("容器删除错误");
            throw exception;
        }
    }
}
