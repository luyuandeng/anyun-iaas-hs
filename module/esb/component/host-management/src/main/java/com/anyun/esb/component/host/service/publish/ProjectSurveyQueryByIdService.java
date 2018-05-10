package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.dto.ProjectInfoQueryDto;
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
 * Created by sxt on 16-8-5.
 */
public class ProjectSurveyQueryByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectSurveyQueryByIdService.class);
    private ProjectService projectService;
    private ProjectDao projectDao;

    public ProjectSurveyQueryByIdService() {
        projectService = new ProjectServiceImpl();
        projectDao = new ProjectDaoImpl();
    }

    @Override
    public String getName() {
        return "projectInfo_query_by_id";
    }

    @Override
    public String getDescription() {
        return "ProjectSurveyQueryByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "POST");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String id = exchange.getIn().getHeader("id", String.class);
        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("param check error");
            exception.setUserMessage("id is empty");
            exception.setMessage("id is empty");
            throw exception;
        }

        ProjectDto projectDto = projectDao.selectProjectById(id);
        if (projectDto == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("param check error");
            exception.setUserMessage("project is not exit");
            exception.setMessage("project is not exit");
            throw exception;
        }

        try {
            LOGGER.debug("param:[{}]", id);
            ProjectInfoQueryDto projectInfoQueryDto = projectService.projectInfoQueryById(id);
            if (projectInfoQueryDto == null)
                return new ProjectInfoQueryDto();
            return projectInfoQueryDto;
        } catch (Exception e) {
            e.getMessage();
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("project query fail");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("project query fail:{" + e.getMessage() + "}");
            throw exception;
        }
    }
}