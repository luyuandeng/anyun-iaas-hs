package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.ProjectService;
import com.anyun.esb.component.host.service.docker.impl.ProjectServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-9-19.
 */
public class ProjectQueryByIdService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProjectQueryByIdService.class);
    private ProjectService projectService;

    public ProjectQueryByIdService() {
        projectService = new ProjectServiceImpl();
    }

    @Override
    public String getName() {
        return "project_query_by_id";
    }

    @Override
    public String getDescription() {
        return "ProjectQueryByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
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
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("服务参数格式化错误[param[id] is empty]");
            exception.setUserMessage("Service param format error");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            LOGGER.debug("param:[{}]", id);
            ProjectDto projectDto = projectService.queryProjectById(id);
            if (projectDto == null)
                return new ProjectDto();
            return projectDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("ProjectQueryByIdService fail[" + e.getMessage() + "]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("ProjectQueryByIdService fail");
            throw exception;
        }
    }
}
