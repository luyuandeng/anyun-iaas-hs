package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.ServiceOperationDto;

import java.util.List;

/**
 * Created by gp on 16-12-14.
 */
public interface ServiceOperationService {
    List<ServiceOperationDto> queryServiceOperationStatus() throws Exception;
}
