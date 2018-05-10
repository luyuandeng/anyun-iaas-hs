package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Response;
import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-9-19.
 */
public class ContainerQueryByProjectAndVolumeService extends AbstractBusinessService {
    public final static Logger LOGGER = LoggerFactory.getLogger(ContainerQueryByProjectAndVolumeService.class);
    private ContainerService containerService;
    private ProjectDao projectDao;

    public ContainerQueryByProjectAndVolumeService() {
        containerService = new ContainerServiceImpl();
        projectDao = new ProjectDaoImpl();
    }

    @Override
    public String getName() {
        return "container_query_by_projectAndVolume";
    }

    @Override
    public String getDescription() {
        return "ContainerQueryByProjectAndVolumeService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);

        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String project = exchange.getIn().getHeader("project", String.class);
        String volume = exchange.getIn().getHeader("volume", String.class);
        if (StringUtils.isEmpty(project)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("服务参数格式化错误[param[project] is empty]");
            exception.setUserMessage("Service param format error");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(volume)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("服务参数格式化错误[param[volume] is empty]");
            exception.setUserMessage("Service param format error");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        ProjectDto projectDto = projectDao.selectProjectById(project);
        if (projectDto == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("服务参数格式化错误[project is not exit]");
            exception.setUserMessage("Service param format error");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            ServiceInvoker invoker = new ServiceInvoker<>();
            invoker.setComponent("anyun-storage");
            invoker.setService("volume_query_by_id");
            Map<String, Object> m = new HashMap<>();
            m.put("id", volume);
            String json = invoker.invoke1(m, null);
            LOGGER.debug("json:{}", json);
            Response<Map<String, Object>> response = JsonUtil.fromJson(Response.class, json);
            Map<String, Object> volumeDto = response.getContent();
            if (volumeDto.get("id") == null) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("服务参数格式化错误[Volume does not exit]");
                exception.setUserMessage("Service param format error");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }
            if (!volumeDto.get("project").toString().equals(project)) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("服务参数格式化错误[Volume does not belong to the project]");
                exception.setUserMessage("Service param format error");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("服务参数格式化错误[" + e.getMessage() + "]");
            exception.setUserMessage("Service param format error");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            LOGGER.debug("Param: project={}  \n   volume={} \n ", project, volume);
            List<ContainerDto> l = containerService.queryContainerForVolumeToBeMounted(project, volume, 0);
            if (l == null)
                return new ArrayList<ContainerDto>();
            return l;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("ContainerQueryByProjectAndVolumeService fail[" + e.getMessage() + "]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("ContainerQueryByProjectAndVolumeService fail");
            throw exception;
        }
    }
}
