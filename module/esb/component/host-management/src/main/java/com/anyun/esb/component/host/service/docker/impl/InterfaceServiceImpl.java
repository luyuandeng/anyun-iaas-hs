package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.InterfaceConfigDto;
import com.anyun.esb.component.host.dao.InterfaceConfigDao;
import com.anyun.esb.component.host.dao.impl.InterfaceConfigDaoImpl;
import com.anyun.esb.component.host.service.docker.InterfaceService;

/**
 * Created by gp on 17-1-3.
 */
public class InterfaceServiceImpl implements InterfaceService {
    private InterfaceConfigDao interfaceConfigDao = new InterfaceConfigDaoImpl();

    @Override
    public InterfaceConfigDto queryInterfaceConfig(String name) throws Exception {
        InterfaceConfigDto dto = interfaceConfigDao.selectConfig(name);
        return dto;
    }
}
