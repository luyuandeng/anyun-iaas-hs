package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.LoggerDto;
import com.anyun.cloud.param.LoggerQueryParam;

import java.util.List;

/**
 * Created by gp on 16-11-2.
 */
public interface LoggerService {

    /**
     * 日志查询
     * @param param
     * @return
     * @throws Exception
     */
    List<LoggerDto> queryLoggerByCondition(LoggerQueryParam param) throws Exception;
}
