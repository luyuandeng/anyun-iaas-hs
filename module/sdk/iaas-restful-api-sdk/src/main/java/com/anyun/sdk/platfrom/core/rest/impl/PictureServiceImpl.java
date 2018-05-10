package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.dto.PictureDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.sdk.platfrom.PictureService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-10-18.
 */
public class PictureServiceImpl implements PictureService {
    public static final String PATH_PICTURE_QUERY_ALL = "/picture/allList";

    public static final String PATH_PICTURE_QUERY_BY_conditions = "/picture/list/conditions";
    @Override
    public List<PictureDto> pictureQueryAll(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId",userUniqueId);
        String response = rsClient.query(PATH_PICTURE_QUERY_ALL, params);
        List<PictureDto> requests =  ResourceClient.convertToListObject(PictureDto.class,response);
        return requests;
    }

    @Override
    public PageDto<PictureDto> getPageListConditions(CommonQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(param.asJson());
        String response = rsClient.query(PATH_PICTURE_QUERY_BY_conditions+"/"+param.asJson(), new HashMap<>());
        PageDto<PictureDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }
}
