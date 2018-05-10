package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.dto.SecurityGroupDto;
import com.anyun.cloud.param.SecurityGroupCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.SecurityGroupDao;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.dao.impl.SecurityGroupDaoImpl;
import com.anyun.esb.component.host.service.docker.SecurityGroupService;
import com.anyun.esb.component.host.service.docker.impl.SecurityGroupServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-10-17.
 */
public class SecurityGroupCreateService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(SecurityGroupCreateService.class);
    private SecurityGroupService securityGroupService;
    private ProjectDao projectDao;
    private SecurityGroupDao securityGroupDao;

    public SecurityGroupCreateService() {
        securityGroupService = new SecurityGroupServiceImpl();
        projectDao = new ProjectDaoImpl();
        securityGroupDao=new SecurityGroupDaoImpl();
    }

    @Override
    public String getName() {
        return "securityGroup_create";
    }

    @Override
    public String getDescription() {
        return "SecurityGroupCreateService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("postBody is empty");
            throw exception;
        }
        SecurityGroupCreateParam param = JsonUtil.fromJson(SecurityGroupCreateParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("param is empty");
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

        if (StringUtils.isEmpty(param.getProject())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[project] is empty");
            exception.setUserMessage("服务参数 [project]  is  empty");
            exception.setUserTitle("参数 project 验证失败");
            throw exception;
        }

        ProjectDto projectDto = projectDao.selectProjectById(param.getProject());
        if (projectDto == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("项目:[{" + param.getProject() + "]}不存在");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getPort())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[port] is empty");
            exception.setUserMessage("service param[port] is empty");
            exception.setUserTitle("参数 port 验证失败");
            throw exception;
        }

        SecurityGroupDto securityGroupDto=securityGroupDao.securityGroupSelectByProjectAndPort(param.getProject(),param.getPort());
        if(securityGroupDto!=null && !StringUtils.isEmpty(securityGroupDto.getLabel())){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("The  port:["+param.getPort()+"]  already exists ");
            exception.setUserMessage("The  port:["+param.getPort()+"]  already exists ");
            exception.setUserTitle("参数 port 验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getIpOrSegment())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[ipOrSegment] is empty");
            exception.setUserMessage("service param[ipOrSegment] is empty");
            exception.setUserTitle("参数 ipOrSegment 验证失败");
            throw exception;
        }

        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";//IP正则表达式
        if(!param.getIpOrSegment().matches(regex)){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[ipOrSegment] is Error IP");
            exception.setUserMessage("service param[ipOrSegment] Error IP");
            exception.setUserTitle("参数 ipOrSegment 是错误IP");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getRule())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[rule] is empty");
            exception.setUserMessage("service param[rule] is empty");
            exception.setUserTitle("参数 rule 验证失败");
            throw exception;
        }

        if (!param.getRule().equals("ALLOW_ACCESS")) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [rule]  unknown");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            return securityGroupService.securityGroupCreate(param);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("securityGroupCreate fail");
            exception.setUserMessage("securityGroupCreate fail[" + exception + "]");
            exception.setUserTitle("securityGroupCreate fail");
            throw exception;
        }
    }
}