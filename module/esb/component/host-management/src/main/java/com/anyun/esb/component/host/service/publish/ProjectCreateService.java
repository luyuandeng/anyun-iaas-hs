package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.param.ProjectCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
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
 * Created by sxt on 16-7-13.
 */
public class ProjectCreateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectCreateService.class);
    public ProjectService projectService;

    public ProjectCreateService() {
        projectService = new ProjectServiceImpl();
    }

    @Override
    public String getName() {
        return "project_create";
    }

    @Override
    public String getDescription() {
        return "project create service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        ProjectCreateParam param = JsonUtil.fromJson(ProjectCreateParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is null");
            exception.setUserMessage("param is null");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"PUT");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        if (param.getSpace() <= 0) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("project[space]<=0");
            exception.setUserMessage("project[space]<=0");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            ProjectDto projectDto= projectService.createProject(param);
            return projectDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Project create fail:" + e.getMessage());
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("Project create fail");
            throw exception;
        }
    }
}
