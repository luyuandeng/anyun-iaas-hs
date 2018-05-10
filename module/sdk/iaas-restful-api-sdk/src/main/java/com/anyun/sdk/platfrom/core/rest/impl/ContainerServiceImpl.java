package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.ContainerService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-8-3.
 */
public class ContainerServiceImpl implements ContainerService {
    public static final String PATH_CONTAINERS_START = "/containers/start";
    public static final String PATH_CONTAINERS_STOP = "/containers/stop";
    public static final String PATH_CONTAINERS_DELETE = "/containers/delete";
    public static final String PATH_CONTAINERS_DETAILS = "/containers/details";
    public static final String PATH_CONTAINERS_QUERY_BY_IMAGE = "/containers/queryByImage";
    public static final String PATH_CONTAINERS_QUERY_BY_NETLABEL = "/containers/queryByNetLabel";
    public static final String PATH_CONTAINERS_QUERY_BY_PROJECT = "/containers/queryByProject";
    public static final String PATH_CONTAINERS_QUERY_BY_PROJECT_AND_VOLUME = "/containers/queryByProjectAndVolume";
    public static final String PATH_CONTAINERS_CREATE = "/containers/create";
    public static final String PATH_CONTAINERS_QUERY_BY_PROJECT_AND_TYPE = "/scontainer/queryByProjectAndType";
    public static final String PATH_CONTAINERS_LIST = "/containers/list";
    public static final String PATH_CONTAINERS_LIST_QUERY = "/containers/getContainerList";
    public static final String PATH_CONTAINERS_OPERATION = "/containers/operation";
    public static final String PATH_CONTAINERS_CREATE_BY_LIST = "/containers/batchCreate";
    public static final String PATH_CONTAINER_CHANGE_CALCULATION_SCHEME = "/containers/scheme/calculation/change";
    public static final String PATH_CONTAINER_CHANGE_DISK_SCHEME = "/containers/scheme/disk/change";

    @Override
    public Status<String> startContainer(ContainerStartParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_CONTAINERS_START, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public Status<String> stopContainer(ContainerStopParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_CONTAINERS_STOP, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public Status<String> deleteContainer(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.delete(PATH_CONTAINERS_DELETE + "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public ContainerDto queryContainerById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_CONTAINERS_DETAILS + "/" + id, param);
        ContainerDto requests = ResourceClient.convertToAnyunEntity(ContainerDto.class, response);
        return requests;
    }

    @Override
    public List<ContainerDto> queryContainerByImage(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_CONTAINERS_QUERY_BY_IMAGE, param);
        List<ContainerDto> requests = ResourceClient.convertToListObject(ContainerDto.class, response);
        return requests;
    }

    @Override
    public List<ContainerDto> queryContainerByNetLabel(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_CONTAINERS_QUERY_BY_NETLABEL, param);
        List<ContainerDto> requests = ResourceClient.convertToListObject(ContainerDto.class, response);
        return requests;
    }

    @Override
    public List<ContainerDto> queryContainerByProject(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_CONTAINERS_QUERY_BY_PROJECT, param);
        List<ContainerDto> requests = ResourceClient.convertToListObject(ContainerDto.class, response);
        return requests;
    }

    @Override
    public ContainerDto createContainerByCondition(ContainerCreateByConditionParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_CONTAINERS_CREATE, param.asJson());
        ContainerDto requests = ResourceClient.convertToAnyunEntity(ContainerDto.class, response);
        return requests;
    }

    @Override
    public List<ContainerDto> queryContainerByProjectAndType(String project, int type, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("project", project);
        param.put("type", type);
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_CONTAINERS_QUERY_BY_PROJECT_AND_TYPE, param);
        List<ContainerDto> requests = ResourceClient.convertToListObject(ContainerDto.class, response);
        return requests;
    }

    @Override
    public List<ContainerDto> queryContainer(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("subMethod", subMethod);
        param.put("subParameters", subParameters);
        String response = rsClient.query(PATH_CONTAINERS_LIST, param);
        List<ContainerDto> requests = ResourceClient.convertToListObject(ContainerDto.class, response);
        return requests;
    }

    @Override
    public PageDto<ContainerDto> getContainerList(CommonQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.query(PATH_CONTAINERS_LIST_QUERY + "/" + param.asJson(), new HashMap<>());
        PageDto<ContainerDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public Status<String> operationContainer(ContainerOpParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_CONTAINERS_OPERATION, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public List<ContainerDto> createContainer(List<ContainerCreateByConditionParam> param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_CONTAINERS_CREATE_BY_LIST, JsonUtil.toJson(param));
        List<ContainerDto> requests = ResourceClient.convertToListObject(ContainerDto.class, response);
        return requests;
    }

    @Override
    public Status<String> changeCalculationScheme(ContainerChangeCalculationSchemeParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_CONTAINER_CHANGE_CALCULATION_SCHEME, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public Status<String> changeDiskScheme(ContainerChangeDiskSchemeParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_CONTAINER_CHANGE_DISK_SCHEME, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }
}
