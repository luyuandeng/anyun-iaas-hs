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
 * Created by sxt on 16-7-12.
 */
public class ProjectQueryByUserUniqueIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectQueryByUserUniqueIdService.class);
    public ProjectService projectService;

    public ProjectQueryByUserUniqueIdService() {
        projectService = new ProjectServiceImpl();
    }

    @Override
    public String getName() {
        return "project_query_by_userUniqueId";
    }

    @Override
    public String getDescription() {
        return "project query byCondition service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);

        if (StringUtils.isEmpty(userUniqueId)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("userUniqueId is empty");
            exception.setUserMessage("userUniqueId is  empty");
            exception.setUserTitle("userUniqueId param error");
            throw exception;
        }

        List<ProjectDto> list =null;
        try {
        if ("admin".equals(userUniqueId)) {
            list=null;
            projectService.queryProjectByCondition();
        }else {
            list = projectService.queryProjectByCondition(userUniqueId);
            if (list == null)
                return new ArrayList<ProjectDto>();
        }
           return  list;
        } catch (Exception e) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setUserTitle("Project query fail");
                exception.setUserMessage(e.getMessage());
                exception.setMessage("Project query fail:{" + e.getMessage() + "}");
                throw exception;
            }

    }
}

