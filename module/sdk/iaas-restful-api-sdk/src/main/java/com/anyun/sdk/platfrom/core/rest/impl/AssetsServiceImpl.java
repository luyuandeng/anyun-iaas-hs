package com.anyun.sdk.platfrom.core.rest.impl;


import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.AssetsCreateParam;
import com.anyun.cloud.param.AssetsUpdateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.AssetsService;

import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AssetsServiceImpl implements AssetsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetsServiceImpl.class);
    private static final String PATH_ASSETS_QUERY_BY_ID = "/assets/details";
    private static final String PATH_ASSETS_QUERY_BY_DEVICECATEGORY = "/assets/list";
    private static final String PATH_ASSETS_QUERY_BY_CONDITIONS = "/assets/base/conditions";
    private static final String PATH_ASSETS_DELETE_BY_ID = "/assets/delete";
    private static final String PATH_ASSETS_CREATE = "/assets/create";
    private static final String PATH_ASSETS_UPDATE = "/assets/update";
    private static final String PATH_ASSETS_DELETE_BY_LIST = "/assets/deleteList";

    @Override
    public AssetsDto queryById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        LOGGER.debug("PARAMS:"+param);
        String response = rsClient.query(PATH_ASSETS_QUERY_BY_ID+"/"+id, param);
        AssetsDto requests = ResourceClient.convertToAnyunEntity(AssetsDto.class,response);
        return requests;
    }

    @Override
    public List<AssetsDto> getList(String deviceCategory, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_ASSETS_QUERY_BY_DEVICECATEGORY+"/"+deviceCategory, param);
        List<AssetsDto> requests = ResourceClient.convertToListObject(AssetsDto.class, response);
        return requests;
    }

    @Override
    public PageDto<AssetsDto> getPageListConditions(CommonQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(param.asJson());
        String response = rsClient.query(PATH_ASSETS_QUERY_BY_CONDITIONS+"/"+param.asJson(), new HashMap<>());
        PageDto<AssetsDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public Status<String> deleteAssets(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        LOGGER.debug("PARAMS:"+param);
        String response = rsClient.delete(PATH_ASSETS_DELETE_BY_ID+ "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public List<AssetsDto> createAssets(List<AssetsCreateParam> param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_ASSETS_CREATE, JsonUtil.toJson(param));
        List<AssetsDto> requests = ResourceClient.convertToListObject(AssetsDto.class, response);
        return requests;
    }

    @Override
    public AssetsDto updateArea(AssetsUpdateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_ASSETS_UPDATE, param.asJson());
        AssetsDto requests = ResourceClient.convertToAnyunEntity(AssetsDto.class, response);
        return requests;
    }

    @Override
    public Status<String> deleteListAssets(List<String> list, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        String ids="";
        Iterator<String> iterator=list.iterator();
        StringBuffer  stringBuffer=new StringBuffer();
        while(iterator.hasNext()){
            String str=iterator.next();
            if("".equals(str))
                continue;
            stringBuffer=stringBuffer.append(str).append(",");
        }
        if(stringBuffer.length()==0)
            ids=stringBuffer.toString();
        else
            ids=stringBuffer.substring(0,stringBuffer.lastIndexOf(","));
        String response = rsClient.delete(PATH_ASSETS_DELETE_BY_LIST+ "/" + ids, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }
}
