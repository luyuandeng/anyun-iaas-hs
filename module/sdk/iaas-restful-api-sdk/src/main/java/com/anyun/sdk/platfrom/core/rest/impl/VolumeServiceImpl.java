package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerVolumeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.param.*;
import com.anyun.sdk.platfrom.VolumeService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 17-5-4.
 */
public class VolumeServiceImpl implements VolumeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VolumeServiceImpl.class);

    public static final String PATH_CREATE_VOLUME = "/volume/create";
    public static final String PATH_UPDATE_VOLUME = "/volume/update";
    public static final String PATH_DELETE_VOLUME_BY_ID = "/volume/delete";
    public static final String PATH_QUERY_VOLUME_BY_ID = "/volume/details";
    public static final String PATH_CONTAINER_MOUNT_VOLUME = "/volume/containerMountVolume";
    public static final String PATH_CONTAINER_UNINSTALL_VOLUME = "/volume/containerUninstallVolume";
    public static final String PATH_QUERY_VOLUME = "/volume/list";
    public static final String PATH_QUERY_VOLUME_BY_STORAGE="/volume/queryVolumeByStorage";
    public static final String PATH_QUERY_VOLUME_LIST="/volume/queryVolumeList";
    public static final String PATH_QUERY_CONTAINER_MOUNT_VOLUME_INFO="/volume/list/containerMountVolumeInfo";

    @Override
    public VolumeDto volumeCreate(VolumeCreateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_CREATE_VOLUME, param.asJson());
        VolumeDto requests = ResourceClient.convertToAnyunEntity(VolumeDto.class, response);
        return requests;
    }


    @Override
    public VolumeDto volumeUpdate(VolumeUpdateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_UPDATE_VOLUME, param.asJson());
        VolumeDto requests = ResourceClient.convertToAnyunEntity(VolumeDto.class, response);
        return requests;
    }


    @Override
    public Status<String> volumeDeleteById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.delete(PATH_DELETE_VOLUME_BY_ID + "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }


    @Override
    public VolumeDto volumeQueryById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_VOLUME_BY_ID+"/"+id, param);
        VolumeDto requests = ResourceClient.convertToAnyunEntity(VolumeDto.class, response);
        return requests;
    }

    @Override
    public Status<String> containerMountVolume(ContainerMountVolumeParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        LOGGER.debug("param:" + param.asJson());
        String response = rsClient.post(PATH_CONTAINER_MOUNT_VOLUME, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }


    @Override
    public Status<String> containerUninstallVolume(ContainerUninstallVolumeParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        LOGGER.debug("param:" + param.asJson());
        String response = rsClient.post(PATH_CONTAINER_UNINSTALL_VOLUME, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public List<VolumeDto> volumeQuery(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("subMethod", subMethod);
        param.put("subParameters", subParameters);
        String response = rsClient.query(PATH_QUERY_VOLUME, param);
        List<VolumeDto> requests = ResourceClient.convertToListObject(VolumeDto.class, response);
        return requests;
    }

    @Override
    public List<VolumeDto> queryVolumeByStroage(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_VOLUME_BY_STORAGE + "/" + id, param);
        List<VolumeDto> requests = ResourceClient.convertToListObject(VolumeDto.class, response);
        return requests;
    }

    @Override
    public PageDto<VolumeDto> queryVolumeList(CommonQueryParam commonQueryParam) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(commonQueryParam.asJson());
        String response = rsClient.query(PATH_QUERY_VOLUME_LIST+"/"+commonQueryParam.asJson(), new HashMap<>());
        PageDto<VolumeDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public List<ContainerVolumeDto> getContainerMountVolumeInfo(String container, String volume, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String,Object>  params =new HashMap<>();
        params.put("container",container);
        params.put("volume",volume);
        params.put("userUniqueId",userUniqueId);
        String response = rsClient.query(PATH_QUERY_CONTAINER_MOUNT_VOLUME_INFO, params);
        List<ContainerVolumeDto> requests = ResourceClient.convertToListObject(ContainerVolumeDto.class, response);
        return requests;
    }
}
