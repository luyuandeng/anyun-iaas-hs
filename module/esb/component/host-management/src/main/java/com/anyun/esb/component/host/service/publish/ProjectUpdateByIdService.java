package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.param.ProjectUpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
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
 * Created by sxt on 16-7-13.
 */
public class ProjectUpdateByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectUpdateByIdService.class);
    public ProjectService projectService;
    public ProjectDao projectDao;

    public ProjectUpdateByIdService() {
        projectService = new ProjectServiceImpl();
        projectDao = new ProjectDaoImpl();
    }

    @Override
    public String getName() {
        return "project_update_by_id";
    }

    @Override
    public String getDescription() {
        return "project updateById  service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]",postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("param check error");
            throw exception;
        }

        ProjectUpdateParam param = JsonUtil.fromJson(ProjectUpdateParam.class, postBody);
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is null");
            exception.setUserMessage("param is null");
            exception.setUserTitle("param check error");
            throw exception;
        }

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("param check error");
            exception.setUserMessage("params[project] is empty");
            exception.setMessage("params[project] is empty");
            throw exception;
        }

        ProjectDto projectDto = projectDao.selectProjectById(param.getId());
        if (projectDto == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("param check error");
            exception.setUserMessage("project is not exist");
            exception.setMessage("project is not exist");
            throw exception;
        }

        if (param.getSpace() < projectDto.getSpace()) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("param check error");
            exception.setUserMessage("param.getSpace() [" + param.getSpace() + "] < projectDto.getSpace()[" + projectDto.getSpace() + "]");
            exception.setMessage("param.getSpace() [" + param.getSpace() + "] < projectDto.getSpace()[" + projectDto.getSpace() + "]");
            throw exception;
        }

        try {
            LOGGER.debug("param:[{}]", param.asJson());
            ProjectDto projectDto1 =projectService.updateProject(param);
            return projectDto1;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("Project update fail");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("Project update fail:{" + e.getMessage() + "}");
            throw exception;
        }
    }
}
