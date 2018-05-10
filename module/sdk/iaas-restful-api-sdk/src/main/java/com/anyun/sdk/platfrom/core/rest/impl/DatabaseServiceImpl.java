package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DatabaseDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.DatabaseCreateParam;
import com.anyun.sdk.platfrom.DatabaseService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sxt on 7/4/17.
 */
public class DatabaseServiceImpl implements DatabaseService {
    public static final String PATH_DB_DETAILS_QUERY = "/db/details";
    public static final String PATH_DB_PAGELIST_QUERY = "db/getPageList";
    public static final String PATH_DB_DETETE = "/db/delete";
    public static final String PATH_DB_CREATE = "/db/create";

    @Override
    public DatabaseDto getDetails(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_DB_DETAILS_QUERY + "/" + id, param);
        DatabaseDto requests = ResourceClient.convertToAnyunEntity(DatabaseDto.class, response);
        return requests;
    }

    @Override
    public PageDto<DatabaseDto> getPageListByCondition(CommonQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.query(PATH_DB_PAGELIST_QUERY + "/" + param.asJson(), new HashMap<String, Object>());
        PageDto<DatabaseDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public Status<String> delete(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.delete(PATH_DB_DETETE + "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }


    @Override
    public DatabaseDto create(DatabaseCreateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_DB_CREATE, param.asJson());
        DatabaseDto requests = ResourceClient.convertToAnyunEntity(DatabaseDto.class, response);
        return requests;
    }
}
