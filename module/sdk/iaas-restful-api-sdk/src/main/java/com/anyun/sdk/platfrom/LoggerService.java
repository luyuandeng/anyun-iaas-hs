package com.anyun.sdk.platfrom;

import com.anyun.cloud.dto.LoggerDto;
import com.anyun.cloud.param.LoggerQueryParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by gp on 16-11-22.
 */
public interface LoggerService {

    /**
     * 日志查询
     * @param param
     * @return
     * @throws RestfulApiException
     */
    List<LoggerDto> queryLoggerByCondition(LoggerQueryParam param) throws RestfulApiException;
}
