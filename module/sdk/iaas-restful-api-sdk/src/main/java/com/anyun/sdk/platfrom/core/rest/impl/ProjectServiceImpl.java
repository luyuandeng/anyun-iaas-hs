package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.PictureDto;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.dto.ProjectInfoQueryDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ProjectCreateParam;
import com.anyun.cloud.param.ProjectUpdateParam;
import com.anyun.sdk.platfrom.ProjectService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-7-13.
 */
public class ProjectServiceImpl implements ProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectServiceImpl.class);
    public static final String PATH_PROJECT_CREATE = "/project/create";
    public static final String PATH_PROJECT_UPDATE = "/project/update";
    public static final String PATH_PROJECT_DELETE = "/project/delete";
    public static final String PATH_PROJECTINFO_QUERY_BY_ID = "/project/queryProjectInfoById";
    public static final String PATH_PROJECT_QUERY_BY_ID = "/project/details";
    public static final String PATH_PROJECT_QUERY_BY_USERUNIQUEID = "/project/list";
    public static final String PATH_PROJECT_QUERY = "/project/projectQuery";

    public static final String PATH_PROJECT_QUERY_BY_conditions = "/project/list/conditions";


    @Override
    public List<ProjectDto> queryProjectByCondition(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        String response = rsClient.query(PATH_PROJECT_QUERY_BY_USERUNIQUEID + "/" + userUniqueId, params);
        List<ProjectDto> requests = ResourceClient.convertToListObject(ProjectDto.class, response);
        return requests;
    }

    @Override
    public ProjectDto createProject(ProjectCreateParam params) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_PROJECT_CREATE, params.asJson());
        ProjectDto requests = ResourceClient.convertToAnyunEntity(ProjectDto.class, response);
        return requests;
    }

    @Override
    public ProjectDto updateProject(ProjectUpdateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_PROJECT_UPDATE, param.asJson());
        ProjectDto requests = ResourceClient.convertToAnyunEntity(ProjectDto.class, response);
        return requests;
    }

    @Override
    public Status<String> deleteProjectByProjectId(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.delete(PATH_PROJECT_DELETE + "/" + id, params);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public ProjectInfoQueryDto queryProjectInfoById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_PROJECTINFO_QUERY_BY_ID, params);
        ProjectInfoQueryDto requests = ResourceClient.convertToAnyunEntity(ProjectInfoQueryDto.class, response);
        return requests;
    }

    @Override
    public ProjectDto queryProjectById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_PROJECT_QUERY_BY_ID + "/" + id, params);
        ProjectDto requests = ResourceClient.convertToAnyunEntity(ProjectDto.class, response);
        return requests;
    }

    @Override
    public List<ProjectDto> projectQuery(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        params.put("subMethod", subMethod);
        params.put("subParameters", subParameters);
        String response = rsClient.query(PATH_PROJECT_QUERY, params);
        List<ProjectDto> requests = ResourceClient.convertToListObject(ProjectDto.class, response);
        return requests;
    }

    @Override
    public PageDto<ProjectDto> getPageListConditions(CommonQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(param.asJson());
        String response = rsClient.query(PATH_PROJECT_QUERY_BY_conditions+"/"+param.asJson(), new HashMap<>());
        PageDto<ProjectDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }
}
