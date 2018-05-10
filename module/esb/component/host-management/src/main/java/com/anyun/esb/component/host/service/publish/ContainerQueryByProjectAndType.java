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

import java.util.List;

/**
 * Created by gp on 16-10-25.
 */
public class ContainerQueryByProjectAndType extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerQueryByProjectAndType.class);
    private ContainerService containerService;

    public ContainerQueryByProjectAndType() {
        containerService = new ContainerServiceImpl();
    }

    @Override
    public String getName() {
        return "container_query_by_projectAndType";
    }

    @Override
    public String getDescription() {
        return "Query Container By Project And Type";
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

        String project = exchange.getIn().getHeader("project", String.class);
        LOGGER.debug("project:[{}]", project);
        if (StringUtils.isEmpty(project)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("project is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("param is  empty");
            throw exception;
        }
        int type = exchange.getIn().getHeader("type", Integer.class);
        LOGGER.debug("type:[{}]", type);

        try {
            List<ContainerDto> list = containerService.queryContainerToBePublished(project, "WEB", type);
            return list;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("根据 项目及类型 查询容器 失败");
            throw exception;
        }
    }
}
