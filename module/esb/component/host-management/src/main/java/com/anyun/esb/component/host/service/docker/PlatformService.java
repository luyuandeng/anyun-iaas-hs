package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.PlatformDto;
import com.anyun.cloud.param.PlatformCreateParam;
import com.anyun.cloud.param.PlatformUpdateParam;

import java.util.List;

/**
 * Created by sxt on 16-10-13.
 */
public interface PlatformService {
    void platformCreate(PlatformCreateParam param) throws Exception;

    void platformUpdate(PlatformUpdateParam param) throws Exception;

    void platformSetAsDefault(String id) throws Exception;

    void platformDelete(String id) throws Exception;

    PlatformDto platformQueryById(String id) throws Exception;

    List<PlatformDto> platformQueryAll() throws Exception;

    PlatformDto platformQueryDefault() throws Exception;

    List<PlatformDto> logicCenterQuery(String subMethod, String subParameters) throws Exception;
}