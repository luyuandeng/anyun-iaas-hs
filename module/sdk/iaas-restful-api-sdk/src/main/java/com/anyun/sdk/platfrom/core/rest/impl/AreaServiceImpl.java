package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AreaDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.AreaCreateParam;
import com.anyun.cloud.param.AreaUpdateParam;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.AreaService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 17-3-28.
 */
public class AreaServiceImpl implements AreaService{
    private static final Logger LOGGER = LoggerFactory.getLogger(AreaServiceImpl.class);

    public static final String PATH_AREA_CREATE = "/area/create";
    public static final String PATH_AREA_DELETE = "/area/delete";
    public static final String PATH_AREA_UPDATE = "/area/update";
    public static final String PATH_AREA_CHANGESTATUS = "/area/changeStatus";
    public static final String PATH_AREA_QUERY_BY_ID = "/area/details";
    public static final String PATH_AREA_QUERY_BY_IMAGE = "/area/list";
    public static final String PATH_AREA_QUERY_BY_conditions = "/area/list/conditions";

    @Override
    public Status<String> createArea(AreaCreateParam param) throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_AREA_CREATE, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }


    @Override
    public  Status<String> deleteArea(String id, String userUniqueId)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        LOGGER.debug("PARAMS:"+param);
        String response = rsClient.delete(PATH_AREA_DELETE+ "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public  Status<String>  updateArea (AreaUpdateParam param)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_AREA_UPDATE, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public  Status<String> changeAreaStatus(AreaUpdateParam param)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_AREA_CHANGESTATUS, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public AreaDto queryById(String id, String userUniqueId)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId",userUniqueId);
        LOGGER.debug("PARAMS:"+param);
        String response = rsClient.query(PATH_AREA_QUERY_BY_ID+"/"+id, param);
        AreaDto requests = ResourceClient.convertToAnyunEntity(AreaDto.class,response);
        return requests;
    }

    @Override
    public List<AreaDto> getList(String type, String status,String userUniqueId)throws RestfulApiException{
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("type",type);
        param.put("status", status);
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_AREA_QUERY_BY_IMAGE, param);
        List<AreaDto> requests = ResourceClient.convertToListObject(AreaDto.class, response);
        return requests;
    }

    @Override
    public PageDto<AreaDto> getPageListConditions(CommonQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        System.out.print(param.asJson());
        String response = rsClient.query(PATH_AREA_QUERY_BY_conditions+"/"+param.asJson(), new HashMap<>());
        PageDto<AreaDto> requests = ResourceClient.convertToAnyunEntity(PageDto.class, response);
        return requests;
    }
}
