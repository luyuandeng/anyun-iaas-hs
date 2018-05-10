package com.anyun.esb.component.host.service.publish;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-8-4.
 */
public class ContainerQueryByImageService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerQueryByImageService.class);
    private ContainerService containerService;
    private ContainerDao containerDao;

    public ContainerQueryByImageService() {
        containerService = new ContainerServiceImpl();
        containerDao = new ContainerDaoImpl();
    }

    @Override
    public String getName() {
        return "container_query_by_image";
    }

    @Override
    public String getDescription() {
        return "ContainerQueryByImageService";
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

        String image = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("image:[{}]", image);
        if (StringUtils.isEmpty(image)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("image is empty");
            exception.setUserMessage("image is empty");
            exception.setUserTitle("param error");
            throw exception;
        }

        if (containerDao.selectRegistImageInfoById(image) == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("image is not exit");
            exception.setUserMessage("image is not exit");
            exception.setUserTitle("param error");
            throw exception;
        }
        try {
            List<ContainerDto> dtos = containerService.queryContainerByImage(image, 0);
            if (dtos == null)
                return new ArrayList<ContainerDto>();
            return dtos;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("根据镜像查询容器 失败");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("根据镜像查询容器 失败");
            throw exception;
        }
    }
}
