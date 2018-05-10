package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.DiskSchemeCreateParam;
import com.anyun.sdk.platfrom.DiskSchemeService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jt on 17-11-23.
 */
public class DiskSchemeServiceImpl implements DiskSchemeService{

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskSchemeServiceImpl.class);


    private static final String PATH_DISKSCHEME_QUERY_BY_ID = "/diskScheme/details";
    private static final String PATH_DISKSCHEME_QUERY_BY_CONDITIONS = "/diskScheme/list/conditions/";
    private static final String PATH_DISKSCHEME_DELETE_BY_ID = "/diskScheme/delete";
    private static final String PATH_DISKSCHEME_CREATE = "/diskScheme/create";
    private static final String PATH_DISKSCHEME_QUERY_LIST ="/diskScheme/list";



    @Override
    public DiskSchemeDto queryDiskSchemeByid(String id,String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        LOGGER.debug("PARAMS:"+param);
        String response = rsClient.query(PATH_DISKSCHEME_QUERY_BY_ID+"/"+id, param);
        DiskSchemeDto requests = ResourceClient.convertToAnyunEntity(DiskSchemeDto.class,response);
        return requests;
    }

    @Override
    public List<DiskSchemeDto> queryDiskSchemeList(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_DISKSCHEME_QUERY_LIST, param);
        List<DiskSchemeDto> requests = ResourceClient.convertToListObject(DiskSchemeDto.class, response);
        return requests;
    }

    @Override
    public PageDto<DiskSchemeDto> queryDiskSchemeDtoBycondition(CommonQueryParam commonQueryParam) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(commonQueryParam.asJson());
        String response = rsClient.query(PATH_DISKSCHEME_QUERY_BY_CONDITIONS+"/"+commonQueryParam.asJson(), new HashMap<>());
        PageDto<DiskSchemeDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public Status<String> deleteDiskSchemeById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        LOGGER.debug("PARAMS:"+param);
        String response = rsClient.delete(PATH_DISKSCHEME_DELETE_BY_ID+ "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public DiskSchemeDto createDiskSchemeDto(DiskSchemeCreateParam diskSchemeCreateParam) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        LOGGER.debug("param:" + diskSchemeCreateParam.asJson());
        String response = rsClient.put(PATH_DISKSCHEME_CREATE, diskSchemeCreateParam.asJson());
        DiskSchemeDto requests = ResourceClient.convertToAnyunEntity(DiskSchemeDto.class, response);
        return requests;
    }
}
