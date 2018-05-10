package com.anyun.sdk.platfrom;

import com.anyun.cloud.dto.ServiceOperationDto;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * Created by gp on 16-12-15.
 */
public interface ServiceOperationService {

    List<ServiceOperationDto> queryServiceOperationStatus(String userUniqueId) throws RestfulApiException;
}
