package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ApplicationInfoDto;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.service.docker.ApplicationService;
import com.anyun.esb.component.host.service.docker.impl.ApplicationServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-10-25.
 */
public class ApplicationQueryByProjectService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationQueryByProjectService.class);
    private ApplicationService applicationService;
    private ProjectDao projectDao;

    public ApplicationQueryByProjectService() {
        applicationService = new ApplicationServiceImpl();
        projectDao = new ProjectDaoImpl();
    }

    @Override
    public String getName() {
        return "application_query_by_project";
    }

    @Override
    public String getDescription() {
        return "ApplicationQueryByProjectService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);

        try {
            UUIDVerify.userRightsVerification(userUniqueId,"POST");
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
            exception.setUserMessage("Service param format error ");
            exception.setMessage("project is empty [" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        ProjectDto projectDto = projectDao.selectProjectById(project);
        if (projectDto == null) {
            LOGGER.debug("project:[{}] is not exit", project);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("项目:[{" + project + "]}不存在");
            throw exception;
        }

        try {
            List<ApplicationInfoDto> list = applicationService.applicationQueryByProject(project);
            if (list == null)
                return new ArrayList<>();
            return list;
        } catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("applicationQueryById  error [" + ex.getMessage() + "]");
            exception.setUserMessage("应用查询失败[" + ex.getMessage() + "]");
            exception.setUserTitle("应用查询失败");
            throw exception;
        }
    }
}
