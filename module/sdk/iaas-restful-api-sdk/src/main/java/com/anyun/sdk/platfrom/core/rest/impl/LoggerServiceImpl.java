package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.dto.LoggerDto;
import com.anyun.cloud.param.LoggerQueryParam;
import com.anyun.sdk.platfrom.LoggerService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-11-22.
 */
public class LoggerServiceImpl implements LoggerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerServiceImpl.class);
    public static final String PATH_LOGGER_LIST="/logger/list";

    /**
     * 日志查询
     * @param param
     * @return
     * @throws RestfulApiException
     */
    @Override
    public List<LoggerDto> queryLoggerByCondition(LoggerQueryParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String,Object> params =new HashMap<String,Object>();
        params.put("ip",param.getIp());
        params.put("starttime",param.getStartTime());
        params.put("endtime",param.getEndTime());
        params.put("keyword",param.getKeyWord());
        params.put("filename",param.getFileName());
        params.put("pagenum",param.getPageNum());
        params.put("pagecount",param.getPageCount());
        params.put("userUniqueId",param.getUserUniqueId());
        LOGGER.debug("params:"+params);
        String response = rsClient.query(PATH_LOGGER_LIST,params);
        List<LoggerDto> requests = ResourceClient.convertToListObject(LoggerDto.class, response);
        return requests;
    }
}
