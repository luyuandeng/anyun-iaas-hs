package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PlatformDto;
import com.anyun.cloud.param.PlatformCreateParam;
import com.anyun.cloud.param.PlatformSetDefaultParam;
import com.anyun.cloud.param.PlatformUpdateParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by gp on 16-10-20.
 */
public interface PlatformService {

    /**
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> platformCreate(PlatformCreateParam param) throws RestfulApiException;

    /**
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> platformUpdate(PlatformUpdateParam param) throws RestfulApiException;

    /**
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> platformSetAsDefault(PlatformSetDefaultParam param) throws RestfulApiException;

    /**
     * OK
     *
     * @param id
     * @param userUniqueId
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> platformDelete(String id, String userUniqueId) throws RestfulApiException;

    /**
     * @param id
     * @param userUniqueId
     * @return PlatformDto
     * @throws RestfulApiException
     */
    PlatformDto platformQueryById(String id, String userUniqueId) throws RestfulApiException;

    /**
     * @param userUniqueId
     * @return PlatformDto
     * @throws RestfulApiException
     */
    PlatformDto platformQueryDefault(String userUniqueId) throws RestfulApiException;

    /**
     * @param userUniqueId
     * @return List<PlatformDto>
     * @throws RestfulApiException
     */
    List<PlatformDto> platformQueryAll(String userUniqueId) throws RestfulApiException;


    /**
     * @param userUniqueId
     * @param subParameters
     * @return List<PlatformDto>
     * @throws RestfulApiException
     * @parm subMethod
     */
    List<PlatformDto> logicCenterQueryQuery(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException;
}
