package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CalculationSchemeCreateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.sdk.platfrom.CalculationSchemeService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by twh-workspace on 17-11-23.
 */
public class CalculationSchemeServiceImpl implements CalculationSchemeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CalculationSchemeServiceImpl.class);

    private static final String PATH_CALCULATIONSCHEME_QUERY_BY_ID = "/calculationScheme/details";
    private static final String PATH_CALCULATIONSCHEME_QUERY_BY_CONDITIONS = "/calculationScheme/list/conditions/";
    private static final String PATH_CALCULATIONSCHEME_DELETE_BY_ID = "/calculationScheme/delete";
    private static final String PATH_CALCULATIONSCHEME_CREATE = "/calculationScheme/create";
    private static final String PATH_CALCULATIONSCHEME_QUERY_LIST ="/calculationScheme/list";

    @Override
    public CalculationSchemeDto queryCalculationSchemeByid(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        LOGGER.debug("PARAMS:"+param);
        String response = rsClient.query(PATH_CALCULATIONSCHEME_QUERY_BY_ID+"/"+id, param);
        CalculationSchemeDto requests = ResourceClient.convertToAnyunEntity(CalculationSchemeDto.class,response);
        return requests;
    }

    @Override
    public List<CalculationSchemeDto> queryCalculationSchemeList(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_CALCULATIONSCHEME_QUERY_LIST, param);
        List<CalculationSchemeDto> requests = ResourceClient.convertToListObject(CalculationSchemeDto.class, response);
        return requests;
    }

    @Override
    public PageDto<CalculationSchemeDto> queryCalculationSchemeDtoBycondition(CommonQueryParam commonQueryParam) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(commonQueryParam.asJson());
        String response = rsClient.query(PATH_CALCULATIONSCHEME_QUERY_BY_CONDITIONS+"/"+commonQueryParam.asJson(), new HashMap<>());
        PageDto<CalculationSchemeDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }

    @Override
    public Status<String> deleteCalculationSchemeById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        LOGGER.debug("PARAMS:"+param);
        String response = rsClient.delete(PATH_CALCULATIONSCHEME_DELETE_BY_ID+ "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public CalculationSchemeDto createCalculationScheme(CalculationSchemeCreateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        LOGGER.debug("param:" + param.asJson());
        String response = rsClient.put(PATH_CALCULATIONSCHEME_CREATE, param.asJson());
        CalculationSchemeDto requests = ResourceClient.convertToAnyunEntity(CalculationSchemeDto.class, response);
        return requests;
    }
}
