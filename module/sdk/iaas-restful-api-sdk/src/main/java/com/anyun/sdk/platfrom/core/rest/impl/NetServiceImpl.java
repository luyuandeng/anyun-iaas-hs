package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.ConnectToNetParam;
import com.anyun.cloud.param.DisconnectFromNetParam;
import com.anyun.cloud.param.NetLabelInfoCreateParam;
import com.anyun.sdk.platfrom.NetService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-7-22.
 */
public class NetServiceImpl implements NetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetServiceImpl.class);
    private Map<String, Object> params;
    public static final String PATH_CONTAINER_CONNECT_TO_NETWORK = "/net/containerConnectToNetwork";
    public static final String PATH_CONTAINER_DISCONNECT_FROM_NETWORK = "/net/containerDisconnectFromNetwork";
    public static final String PATH_GET_NETLABELINFO_DETAILS = "/net/netLabelInfo/details";
    public static final String PATH_GET_NETLABELINFO_LIST = "/net/netLabelInfo/list";
    public static final String PATH_GET_CONTAINERIPINFO_LIST = "/net/containerIpInfo/list";
    public static final String PATH_CONTAINER_IP_QUERY = "/net/getContainerIpList";
    public static final String PATH_NetLabel_QUERY = "/net/getL3NetList";




    @Override
    public NetLabelInfoDto getNetLabelInfoDetails( String label,String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_GET_NETLABELINFO_DETAILS + "/" + label, params);
        NetLabelInfoDto requests = ResourceClient.convertToAnyunEntity(NetLabelInfoDto.class, response);
        return requests;
    }

    @Override
    public List<NetLabelInfoDto> getNetLabelInfoList( String type,String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String responses = rsClient.query(PATH_GET_NETLABELINFO_LIST + "/" + type, params);
        List<NetLabelInfoDto> requests = ResourceClient.convertToListObject(NetLabelInfoDto.class, responses);
        return requests;
    }

    @Override
    public List<ContainerIpInfoDto> getContainerIpInfoList(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        params.put("subMethod", subMethod);
        params.put("subParameters", subParameters);
        String responses = rsClient.query(PATH_GET_CONTAINERIPINFO_LIST, params);
        List<ContainerIpInfoDto> requests = ResourceClient.convertToListObject(ContainerIpInfoDto.class, responses);
        return requests;
    }

    @Override
    public Status<String> containerConnectToNetwork(ConnectToNetParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_CONTAINER_CONNECT_TO_NETWORK, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public Status<String> containerDisconnectFromNetwork(DisconnectFromNetParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_CONTAINER_DISCONNECT_FROM_NETWORK, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public PageDto<ContainerIpInfoDto> getContainerIpList(CommonQueryParam param)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(param.asJson());
        String response = rsClient.query(PATH_CONTAINER_IP_QUERY+"/"+param.asJson(), new HashMap<>());
        PageDto<ContainerIpInfoDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public PageDto<NetLabelInfoDto> QueryNetLabelInfoList(CommonQueryParam param)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(param.asJson());
        String response = rsClient.query(PATH_NetLabel_QUERY+"/"+param.asJson(), new HashMap<>());
        PageDto<NetLabelInfoDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }
}
