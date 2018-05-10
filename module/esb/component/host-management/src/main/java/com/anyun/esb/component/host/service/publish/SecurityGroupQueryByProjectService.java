package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.dto.SecurityGroupDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.service.docker.SecurityGroupService;
import com.anyun.esb.component.host.service.docker.impl.SecurityGroupServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-10-18.
 */
public class SecurityGroupQueryByProjectService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SecurityGroupQueryByProjectService.class);
    private SecurityGroupService securityGroupService;
    private ProjectDao projectDao;

    public SecurityGroupQueryByProjectService() {
        securityGroupService = new SecurityGroupServiceImpl();
        projectDao = new ProjectDaoImpl();
    }

    @Override
    public String getName() {
        return "securityGroup_query_by_project";
    }

    @Override
    public String getDescription() {
        return "SecurityGroupQueryByProjectService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
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
            exception.setMessage("project is empty");
            exception.setUserMessage("project is empty");
            exception.setUserTitle("project is empty");
            throw exception;
        }

        ProjectDto projectDto = projectDao.selectProjectById(project);
        if (projectDto == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("项目:[{" + project + "]}不存在");
            throw exception;
        }

        try {
            List<SecurityGroupDto> list = securityGroupService.securityGroupQueryByProject(project);
            if (list == null)
                return new ArrayList<>();
            for (SecurityGroupDto dto : list){
                SimpleDateFormat date =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String createTime = date.format(dto.getCreateDate());
                dto.setScreateDate(createTime);
            }
            return list;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("SecurityGroupQueryByProject fail");
            exception.setUserMessage("SecurityGroupQueryByProject fail[" + exception + "]");
            exception.setUserTitle("SecurityGroupQueryByProject fail");
            throw exception;
        }
    }
}
