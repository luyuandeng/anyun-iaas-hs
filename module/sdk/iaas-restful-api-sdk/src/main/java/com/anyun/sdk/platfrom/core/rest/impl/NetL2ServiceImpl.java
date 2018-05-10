package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.NetL2InfoDto;
import com.anyun.cloud.param.NetL2CreateParam;
import com.anyun.cloud.param.NetL2UpdateParam;
import com.anyun.sdk.platfrom.NetL2Service;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 17-4-10.
 */
public class NetL2ServiceImpl implements NetL2Service {
    private Map<String, Object> params;
    public static final String PATH_GET_NETL2INFO_DETAILS = "/netL2/details";
    public static final String PATH_GET_NETL2INFO_LIST = "/netL2/list";
    public static final String PATH_NETL2_DELETE = "/netL2/delete";
    public static final String PATH_NETL2_CREATE = "/netL2/create";
    public static final String PATH_NETL2_UPDATE = "/netL2/update";


    @Override
    public NetL2InfoDto getDetails(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_GET_NETL2INFO_DETAILS + "/" + id, params);
        NetL2InfoDto requests = ResourceClient.convertToAnyunEntity(NetL2InfoDto.class, response);
        return requests;
    }

    @Override
    public List<NetL2InfoDto> getNetL2InfoList(String type, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_GET_NETL2INFO_LIST + "/" + type, params);
        List<NetL2InfoDto> requests = ResourceClient.convertToListObject(NetL2InfoDto.class, response);
        return requests;
    }

    @Override
    public Status<String> netL2DeleteById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        String response = rsClient.delete(PATH_NETL2_DELETE+ "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public NetL2InfoDto createNetL2(NetL2CreateParam param,String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_NETL2_CREATE, param.asJson());
        NetL2InfoDto requests = ResourceClient.convertToAnyunEntity(NetL2InfoDto.class, response);
        return requests;
    }

    @Override
    public NetL2InfoDto updateNetL2(NetL2UpdateParam param,String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_NETL2_UPDATE, param.asJson());
        NetL2InfoDto requests = ResourceClient.convertToAnyunEntity(NetL2InfoDto.class, response);
        return requests;
    }
}
