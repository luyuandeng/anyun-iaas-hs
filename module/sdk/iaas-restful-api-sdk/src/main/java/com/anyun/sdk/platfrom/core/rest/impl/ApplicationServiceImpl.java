package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ApplicationInfoDto;
import com.anyun.cloud.dto.ApplicationInfoLoadDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.*;
import com.anyun.sdk.platfrom.ApplicationService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-10-26.
 */
public class ApplicationServiceImpl implements ApplicationService {
    public static final String PATH_APPLICATIONS_DETAILS = "/applications/details";
    public static final String PATH_APPLICATIONS_LIST = "/applications/list";
    public static final String PATH_APPLICATIONS_DELETE = "/applications/delete";
    public static final String PATH_APPLICATIONS_CREATE = "/applications/create";
    public static final String PATH_APPLICATION_QUERY_BY_CONDITIONS = "/applications/list/conditions";
    public static final String PATH_APPLICATION_LOAD_QUERY_BY_CONDITIONS = "/applications/list/load";
    public static final String PATH_APPLICATION_LOAD_OPERATION = "/applications/load/operation";
    public static final String PATH_APPLICATION_LOAD_ADD = "/applications/load/add";
    public static final String PATH_APPLICATION_REPUBLISH="/applications/republish";


    @Override
    public ApplicationInfoDto applicationCreate(ApplicationCreateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_APPLICATIONS_CREATE, param.asJson());
        ApplicationInfoDto requests = ResourceClient.convertToAnyunEntity(ApplicationInfoDto.class, response);
        return requests;
    }

    @Override
    public Status<String> applicationDeleteById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.delete(PATH_APPLICATIONS_DELETE + "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public ApplicationInfoDto applicationQueryById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_APPLICATIONS_DETAILS + "/" + id, param);
        ApplicationInfoDto requests = ResourceClient.convertToAnyunEntity(ApplicationInfoDto.class, response);
        return requests;
    }

    @Override
    public List<ApplicationInfoDto> applicationQueryByProject(String project, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_APPLICATIONS_LIST + "/" + project, param);
        List<ApplicationInfoDto> requests = ResourceClient.convertToListObject(ApplicationInfoDto.class, response);
        return requests;
    }

    @Override
    public PageDto<ApplicationInfoDto> applicationQueryByConditions(CommonQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.query(PATH_APPLICATION_QUERY_BY_CONDITIONS + "/" + param.asJson(), new HashMap<>());
        PageDto<ApplicationInfoDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public PageDto<ApplicationInfoLoadDto> applicationLoadQueryByConditions(CommonQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.query(PATH_APPLICATION_LOAD_QUERY_BY_CONDITIONS + "/" + param.asJson(), new HashMap<>());
        PageDto<ApplicationInfoLoadDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public Status<String> operationLoad(ContainerOpParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_APPLICATION_LOAD_OPERATION, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public List<ApplicationInfoLoadDto> addLoad(ApplicationLoadAddParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_APPLICATION_LOAD_ADD, param.asJson());
        List<ApplicationInfoLoadDto> requests = ResourceClient.convertToListObject(ApplicationInfoLoadDto.class, response);
        return requests;
    }

    @Override
    public ApplicationInfoDto republish(String id) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_APPLICATION_REPUBLISH, id);
        ApplicationInfoDto requests = ResourceClient.convertToAnyunEntity(ApplicationInfoDto.class, response);
        return requests;
    }
}
