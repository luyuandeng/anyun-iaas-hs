package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
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
 * Created by sxt on 16-7-13.
 */
public class ContainerQueryByProjectService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerQueryByProjectService.class);
    private ContainerService containerService;
    private ProjectDao projectDao;

    public ContainerQueryByProjectService() {
        containerService = new ContainerServiceImpl();
        projectDao = new ProjectDaoImpl();
    }

    @Override
    public String getName() {
        return "container_query_by_Project";
    }

    @Override
    public String getDescription() {
        return "ContainerQueryByProjectService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId=exchange.getIn().getHeader("userUniqueId",String.class);
        LOGGER.debug("userUniqueId:[{}]",userUniqueId);

        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        String id = exchange.getIn().getHeader("id", String.class);

        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("project is  empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[id] is empty");
            exception.setUserMessage("服务参数 [id]  is  empty");
            exception.setUserTitle("参数 id 验证失败");
            throw exception;
        }

        ProjectDto projectDto = projectDao.selectProjectById(id);
        if (projectDto == null) {
            LOGGER.debug("project:[{}] is not exit", id);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("项目:[{" + id + "]}不存在");
            throw exception;
        }

        try {
            LOGGER.debug("id:[{}]", id);
            List<ContainerDto> dtos = containerService.queryContainerByProject(id,0);
            if (dtos == null)
                return new ArrayList<ContainerDto>();
            return dtos;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("有项目id 查询 容器失败");
            throw exception;
        }
    }
}

