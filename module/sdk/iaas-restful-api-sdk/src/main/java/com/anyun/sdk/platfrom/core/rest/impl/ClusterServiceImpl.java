package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ClusterDto;
import com.anyun.cloud.param.ClusterChangeStatusParam;
import com.anyun.cloud.param.ClusterCreateParam;
import com.anyun.cloud.param.ClusterUpdateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.ClusterService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jt-workspace on 17-4-6.
 */
public class ClusterServiceImpl implements ClusterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterServiceImpl.class);


    public static final String PATH_Cluster_Query = "/cluster/details";
    public static final String PATH_Cluster_Query_List="/cluster/list";
    public static final String PATH_Cluster_Delete="/cluster/delete";
    public static final String PATH_Cluster_Create="/cluster/create";
    public static final String PATH_Cluster_Update="/cluster/update";
    public static final String PATH_Cluster_Change_Status="/cluster/changeStatus";



    @Override
    public ClusterDto ClusterQueryById(String id, String userUniqueId)throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_Cluster_Query+"/"+id, param);
        ClusterDto  requests = ResourceClient.convertToAnyunEntity(ClusterDto.class, response);
        return requests;
    }

    @Override
    public List<ClusterDto> ClusterQueryListService (String status, String userUniqueId) throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("status",status);
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_Cluster_Query_List+"/"+status, param);
        List<ClusterDto> requests = ResourceClient.convertToListObject(ClusterDto.class, response);
        return requests;
    }

    @Override
    public Status<String> ClusterDeleteService (String id, String userUniqueId)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.delete(PATH_Cluster_Delete+"/"+id, param);
        Status<String>  requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }


    @Override
    public ClusterDto ClusterCreateService (ClusterCreateParam param)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_Cluster_Create, param.asJson());
        ClusterDto  requests = ResourceClient.convertToAnyunEntity(ClusterDto.class, response);
        return requests;
    }

    @Override
    public ClusterDto ClusterUpdateService(ClusterUpdateParam param)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_Cluster_Update, param.asJson());
        ClusterDto  requests = ResourceClient.convertToAnyunEntity(ClusterDto.class, response);
        return requests;
    }



    @Override
    public Status<String> changeStatus(ClusterChangeStatusParam param)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_Cluster_Change_Status, param.asJson());
        Status<String>  requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }
}
