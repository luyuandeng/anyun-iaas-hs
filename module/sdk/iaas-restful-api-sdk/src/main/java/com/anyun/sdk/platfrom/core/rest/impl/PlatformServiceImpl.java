package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PlatformDto;
import com.anyun.cloud.param.PlatformCreateParam;
import com.anyun.cloud.param.PlatformSetDefaultParam;
import com.anyun.cloud.param.PlatformUpdateParam;
import com.anyun.sdk.platfrom.PlatformService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-10-20.
 */
public class PlatformServiceImpl implements PlatformService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformServiceImpl.class);

    public static final String PATH_PLATFORM_CREATE = "/platform/create";
    public static final String PATH_PLATFORM_UPDATE = "/platform/update";
    public static final String PATH_PLATFORM_SET_AS_DEFAULT = "/platform/setAsDefault";
    public static final String PATH_PLATFORM_DELETE = "/platform/delete";
    public static final String PATH_PLATFORM_QUERY_BY_ID = "/platform/queryById";
    public static final String PATH_PLATFORM_QUERY_DEFAULT = "/platform/queryDefault";
    public static final String PATH_PLATFORM_QUERY_ALL = "/platform/queryAll";
    public static final String PATH_LOGICCENTER_QUERY ="/platform/logicCenterQuery";

    @Override
    public Status<String> platformCreate(PlatformCreateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        LOGGER.debug("param:" + param.asJson());
        String response = rsClient.put(PATH_PLATFORM_CREATE, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }


    @Override
    public Status<String> platformUpdate(PlatformUpdateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        LOGGER.debug("param:" + param.asJson());
        String response = rsClient.post(PATH_PLATFORM_UPDATE, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }


    @Override
    public Status<String> platformSetAsDefault(PlatformSetDefaultParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_PLATFORM_SET_AS_DEFAULT, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public Status<String> platformDelete(String id,String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("userUniqueId",userUniqueId);
        String response = rsClient.delete(PATH_PLATFORM_DELETE, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public PlatformDto platformQueryById(String id,String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        param.put("userUniqueId",userUniqueId);
        String response = rsClient.query(PATH_PLATFORM_QUERY_BY_ID, param);
        PlatformDto requests = ResourceClient.convertToAnyunEntity(PlatformDto.class, response);
        return requests;
    }

    @Override
    public PlatformDto platformQueryDefault(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        String response = rsClient.query(PATH_PLATFORM_QUERY_DEFAULT, param);
        PlatformDto requests = ResourceClient.convertToAnyunEntity(PlatformDto.class, response);
        return requests;
    }

    @Override
    public List<PlatformDto> platformQueryAll(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String,Object>  param=new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        String response = rsClient.query(PATH_PLATFORM_QUERY_ALL, param);
        List<PlatformDto> requests = ResourceClient.convertToListObject(PlatformDto.class, response);
        return requests;
    }

    @Override
    public List<PlatformDto> logicCenterQueryQuery(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String,Object>  param=new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        param.put("subMethod",subMethod);
        param.put("subParameters",subParameters);
        String response = rsClient.query(PATH_LOGICCENTER_QUERY, param);
        List<PlatformDto> requests = ResourceClient.convertToListObject(PlatformDto.class, response);
        return requests;
    }

}
