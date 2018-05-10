package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.api.Response;
import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.storage.common.UUIDVerify;
import com.anyun.esb.component.storage.service.storage.StorageService;
import com.anyun.esb.component.storage.service.storage.VolumeService;
import com.anyun.esb.component.storage.service.storage.impl.StorageServiceImpl;
import com.anyun.esb.component.storage.service.storage.impl.VolumeServiceImpl;
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
public class VolumeQueryByProjectAndContainerService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(VolumeQueryByProjectAndContainerService.class);
    private VolumeService volumeService;

    public VolumeQueryByProjectAndContainerService() {
        volumeService = new VolumeServiceImpl();
    }

    @Override
    public String getName() {
        return "volume_query_by_project_and_container_userUniqueId";
    }

    @Override
    public String getDescription() {
        return "VolumeQueryByProjectAndContainerService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId=exchange.getIn().getHeader("userUniqueId",String.class);
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
        String container = exchange.getIn().getHeader("container", String.class);
        if (StringUtils.isEmpty(project)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("服务参数格式化错误[param[project] is empty]");
            exception.setUserMessage("Service param format error");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        if (StringUtils.isEmpty(container)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("服务参数格式化错误[param[container] is empty]");
            exception.setUserMessage("Service param format error");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            ServiceInvoker invoker = new ServiceInvoker<>();
            invoker.setComponent("anyun-host");
            //project_query_by_id
            invoker.setService("project_query_by_id");
            Map<String, Object> m = new HashMap<>();
            m.put("id", project);
            m.put("userUniqueId", userUniqueId);
            String json = invoker.invoke1(m, null);
            LOGGER.debug("json:{}", json);
            Response<Map<String, Object>> response = JsonUtil.fromJson(Response.class, json);
            Map<String, Object> projectDto = response.getContent();
            if (projectDto.get("id")==null) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("服务参数格式化错误[project does not exit]");
                exception.setUserMessage("Service param format error");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }
            //container_query_by_id
            invoker.setService("container_query_by_id");
            m.put("id", container);
            m.put("userUniqueId",userUniqueId);
            json = "";
            json = invoker.invoke1(m, null);
            LOGGER.debug("json:{}", json);
            Response<Map<String, Object>> response1 = JsonUtil.fromJson(Response.class, json);
            Map<String, Object> containerDto = response1.getContent();
            if (containerDto.get("id")==null){
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("服务参数格式化错误[Container does not exit]");
                exception.setUserMessage("Service param format error");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }
            if (!containerDto.get("projectId").toString().equals(project)) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("服务参数格式化错误[Container does not belong to the project]");
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
            LOGGER.debug("Param: project={}  \n   container={} \n ", project, container);
            List<VolumeDto> l = volumeService.queryVolumeByProjectAndContainer(project, container);
            if (l == null)
                return new ArrayList<VolumeDto>();
            return l;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("VolumeQueryByProjectAndContainerService fail[" + e.getMessage() + "]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("VolumeQueryByProjectAndContainerService fail");
            throw exception;
        }
    }
}
