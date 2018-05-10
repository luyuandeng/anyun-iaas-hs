package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.InterfaceConfigDto;

/**
 * Created by gp on 17-1-3.
 */
public interface InterfaceService {
    InterfaceConfigDto queryInterfaceConfig(String name) throws Exception;
}
