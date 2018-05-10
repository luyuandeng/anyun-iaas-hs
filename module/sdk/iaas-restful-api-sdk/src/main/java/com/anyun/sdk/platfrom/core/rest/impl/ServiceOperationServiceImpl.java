package com.anyun.sdk.platfrom.core.rest.impl;


import com.anyun.cloud.dto.ServiceOperationDto;
import com.anyun.sdk.platfrom.ServiceOperationService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-12-15.
 */
public class ServiceOperationServiceImpl implements ServiceOperationService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceOperationServiceImpl.class);

    public static final String PATH_QUERY_SERVICE_OPERATION="/services/allList";
    @Override
    public List<ServiceOperationDto> queryServiceOperationStatus(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId",userUniqueId);
        String response = rsClient.query(PATH_QUERY_SERVICE_OPERATION, params);
        List<ServiceOperationDto> requests = ResourceClient.convertToListObject(ServiceOperationDto.class,response);
        return requests;

    }
}
