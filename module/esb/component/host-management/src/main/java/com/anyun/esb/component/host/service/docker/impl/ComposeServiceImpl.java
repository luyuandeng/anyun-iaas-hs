package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.ComposeCreateParam;
import com.anyun.cloud.param.ContainerCreateByConditionParam;
import com.anyun.cloud.param.ProjectCreateParam;
import com.anyun.cloud.param.VolumeCreateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.host.dao.ComposeDao;
import com.anyun.esb.component.host.dao.impl.ComposeDaoImpl;
import com.anyun.esb.component.host.service.docker.ComposeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-12-6.
 */
public class ComposeServiceImpl implements ComposeService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ComposeServiceImpl.class);
    private ComposeDao composeDao;

    public ComposeServiceImpl() {
        composeDao = new ComposeDaoImpl();
    }

    @Override
    public void deleteComposeById(String userUniqueId, String id) throws Exception {
        ComposeDto composeDto = composeDao.selectComposeById(id);
        if (composeDto == null)
            return;
        ServiceInvoker invoker = new ServiceInvoker<>();
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("userUniqueId", userUniqueId);
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("project_delete_by_id");
        invoker.invoke(param, null);
        composeDao.deleteComposeById(id);
    }

    @Override
    public List<ComposeDto> queryComposeByUserUniqueId(String userUniqueId) throws Exception {
        List<ComposeDto> list = composeDao.selectComposeByUserUniqueId(userUniqueId);
        if (list == null)
            return null;
        for (ComposeDto c : list) {
            ProjectDto projectDto = queryProjectByIdAndUserUniqueId(c.getProject(), userUniqueId);
            c.setProjectDto(projectDto);
        }
        return list;
    }


    @Override
    public void createCompose(ComposeCreateParam param) throws Exception {
        ServiceInvoker invoker = new ServiceInvoker<>();
        //创建项目
        ProjectCreateParam projectCreateParam = new ProjectCreateParam();
        projectCreateParam.setUserUniqueId(param.getUserUniqueId());
        projectCreateParam.setName(param.getName());
        projectCreateParam.setDescript(param.getDescript());
        projectCreateParam.setSpace(param.getSpace());
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("call_project_create");
        Map response = (Map) invoker.invoke(null, projectCreateParam.asJson());
        Status<String> status = JsonUtil.fromJson(Status.class, JsonUtil.toJson(response));
        String project = status.getStatus();

        //json  转 java 对象
        String json = param.getTemplate();
        LOGGER.debug("json:[{}]", json);
        TemplateCompose templateCompose = JsonUtil.fromJson(TemplateCompose.class, json);

        //创建容器
        List<TemplateContainer> templateContainers = templateCompose.getTemplateContainers();
        if (templateContainers != null) {
            for (TemplateContainer c : templateContainers) {
                ContainerCreateByConditionParam param1 = c;
                param1.setProjectId(project);
                param1.setUserUniqueId(param.getUserUniqueId());
                LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
                invoker.setComponent("anyun-host");
                invoker.setService("container_create");
                invoker.invoke(null, param1.asJson());
            }
        }

        //创建卷
        List<TemplateVolume> templateVolumes = templateCompose.getTemplateVolumes();
        if (templateVolumes != null) {
            for (TemplateVolume v : templateVolumes) {
                VolumeCreateParam volumeCreateParam = v;
                v.setProject(project);
                v.setUserUniqueId(param.getUserUniqueId());
                LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
                invoker.setComponent("anyun-storage");
                invoker.setService("volume_create");
                invoker.invoke(null, volumeCreateParam.asJson());
            }
        }

        //创建网络  //TODO  待定
//        List<TemplateNet>  templateNets=templateCompose.getTemplateNets();

        //创建 编排
        ComposeDto composeDto = new ComposeDto();
        composeDto.setProject(project);
        composeDto.setVersion(param.getVersion());
        composeDao.insertCompose(composeDto);
    }

    @Override
    public ComposeDto queryComposeById(String userUniqueId, String id) throws Exception {
        ComposeDto composeDto = composeDao.selectComposeById(id);
        if (composeDto != null) {
            TemplateComposeDto templateComposeDto = new TemplateComposeDto();
            templateComposeDto.setContainerDtos(queryVContainerByProjectAndUserUniqueId(id, userUniqueId));
            templateComposeDto.setVolumeDtos(queryVolumeByIdAndUserUniqueId(id, userUniqueId));
            String template = templateComposeDto.asJson();
            composeDto.setTemplate(template);
            ProjectDto projectDto = queryProjectByIdAndUserUniqueId(id, userUniqueId);
            composeDto.setProjectDto(projectDto);
        }
        return composeDto;
    }

    //根据项目  查询容器信息
    private static List<ContainerDto> queryVContainerByProjectAndUserUniqueId(String id, String userUniqueId) throws Exception {
        ServiceInvoker<List> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("container_query_by_Project");
        Map<String, String> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("id", id);
        List response = invoker.invoke(param, null);
        List<ContainerDto> list = JsonUtil.fromJson(List.class, JsonUtil.toJson(response));
        return list;
    }

    //很据项目  查询卷信息
    private static List<VolumeDto> queryVolumeByIdAndUserUniqueId(String id, String userUniqueId) throws Exception {
        ServiceInvoker<List> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-storage");
        invoker.setService("volume_query_by_project");
        Map<String, String> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("id", id);
        List response = invoker.invoke(param, null);
        List<VolumeDto> list = JsonUtil.fromJson(List.class, JsonUtil.toJson(response));
        return list;
    }


    //根据项目id  查询项目信息
    private static ProjectDto queryProjectByIdAndUserUniqueId(String project, String userUniqueId) throws Exception {
        ServiceInvoker<Map> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("project_query_by_id");
        Map<String, String> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("id", project);
        Map response = invoker.invoke(param, null);
        ProjectDto projectDto = JsonUtil.fromJson(ProjectDto.class, JsonUtil.toJson(response));
        return projectDto;
    }
}
