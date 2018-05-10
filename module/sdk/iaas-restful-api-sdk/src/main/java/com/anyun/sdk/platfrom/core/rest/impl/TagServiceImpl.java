package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.TagDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.TagCreateParam;
import com.anyun.cloud.param.TagUpdateParam;
import com.anyun.sdk.platfrom.TagService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/5/9.
 */
public class TagServiceImpl implements TagService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TagServiceImpl.class);

    public static final String PATH_TAG_QUERY_BY_conditions = "/tag/list/conditions";
    public static final String PATH_TAG_CREATE = "/tag/create";
    public static final String PATH_TAG_DELETE = "/tag/delete";
    public static final String PATH_TAG_UPDATE = "/tag/update";
    public static final String PATH_TAG_DELETE_BY_RESOURCEID = "/tag/deleteByResourceId";

    @Override
    public PageDto<TagDto> getListByconditions(CommonQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(param.asJson());
        String response = rsClient.query(PATH_TAG_QUERY_BY_conditions+"/"+param.asJson(), new HashMap<>());
        PageDto<TagDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public TagDto tagCreate(TagCreateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_TAG_CREATE, param.asJson());
        TagDto requests = ResourceClient.convertToAnyunEntity(TagDto.class, response);
        return requests;
    }

    @Override
    public Status<String> tagDelete(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        String response = rsClient.delete(PATH_TAG_DELETE+ "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public TagDto tagUpdate(TagUpdateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_TAG_UPDATE, param.asJson());
        TagDto requests = ResourceClient.convertToAnyunEntity(TagDto.class, response);
        return requests;
    }

    @Override
    public Status<String> tagDeleteByResourceId(String resourceId, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        String response = rsClient.delete(PATH_TAG_DELETE_BY_RESOURCEID+ "/" + resourceId, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

//    @Override
//    public TagDto queryById(String id, String userUniqueId) throws RestfulApiException {
//        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
//        Map<String, Object> param = new HashMap<>();
//        param.put("userUniqueId",userUniqueId);
//        LOGGER.debug("PARAMS:"+param);
//        String response = rsClient.query(PATH_TAG_QUERY_BY_ID+"/"+id, param);
//        TagDto requests = ResourceClient.convertToAnyunEntity(TagDto.class,response);
//        return requests;
//    }
}
