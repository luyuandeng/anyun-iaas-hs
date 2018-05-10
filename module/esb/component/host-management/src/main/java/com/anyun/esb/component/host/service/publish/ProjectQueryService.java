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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 17-3-9.
 */
public class ProjectQueryService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectQueryService.class);
    private ProjectService projectService;

    public ProjectQueryService () {
        projectService = new ProjectServiceImpl();
    }

    @Override
    public String getName() {
        return "project_query";
    }

    @Override
    public String getDescription() {
        return "ProjectQueryService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "GET");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String subMethod = exchange.getIn().getHeader("subMethod", String.class);
        LOGGER.debug("subMethod:[{}]", subMethod);
        if (StringUtils.isEmpty(subMethod)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("subMethod is empty");
            exception.setUserMessage("subMethod is  empty");
            exception.setUserTitle("subMethod param error");
            throw exception;
        }

        String subParameters = exchange.getIn().getHeader("subParameters", String.class);
        LOGGER.debug("subParameters:[{}]", subParameters);
        if (StringUtils.isEmpty(subParameters)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("subParameters is empty");
            exception.setUserMessage("subParameters is  empty");
            exception.setUserTitle("subParameters param error");
            throw exception;
        }

        try {
            List<ProjectDto> l = projectService.projectQuery(subMethod, subParameters);
            if (l == null || l.isEmpty())
                return new ArrayList<ProjectDto>();
            return l;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("project query fail: [" + e.getMessage() + "]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("查询 项目 失败");
            throw exception;
        }
    }
}
