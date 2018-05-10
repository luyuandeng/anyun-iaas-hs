package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-7-21.
 */
public class ContainerQueryByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerQueryByIdService.class);
    private ContainerService containerService;

    public ContainerQueryByIdService() {
        containerService = new ContainerServiceImpl();
    }

    @Override
    public String getName() {
        return "container_query_by_id";
    }

    @Override
    public String getDescription() {
        return "Container Query ByContainerId Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);

        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String container = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("container:[{}]", container);
        if (StringUtils.isEmpty(container)) {
            LOGGER.debug("container is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("container is empty");
            exception.setUserMessage("container is  empty");
            exception.setUserTitle("container param error");
            throw exception;
        }
        try {
            ContainerDto dtos = containerService.queryContainerById(container, 0);
            if (dtos == null)
                return new ContainerDto();
            return dtos;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("根据 Id 查询容器 失败");
            throw exception;
        }
    }
}
