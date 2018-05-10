package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.service.docker.ProjectService;
import com.anyun.esb.component.host.service.docker.impl.ProjectServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-7-12.
 */
public class ProjectDeleteByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDeleteByIdService.class);
    public ProjectService projectService;
    public ProjectDao projectDao;

    public ProjectDeleteByIdService() {
        projectService = new ProjectServiceImpl();
        projectDao = new ProjectDaoImpl();
    }

    @Override
    public String getName() {
        return "project_delete_by_id";
    }

    @Override
    public String getDescription() {
        return "project deleteById service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String id = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("id:[{}]",id);
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

        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("Parameter check error");
            exception.setUserMessage("id is empty");
            exception.setMessage("id is empty");
            throw exception;
        }
        ProjectDto projectDto = projectDao.selectProjectById(id);
        if (projectDto == null) {
            LOGGER.debug("Project:[{}] is not exit", id);
            return new Status<String>("success");
        }
        try {
            projectService.deleteProject(id);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("Project delete fail");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("Project delete fail:{" + e.getMessage() + "}");
            throw exception;
        }
    }
}
