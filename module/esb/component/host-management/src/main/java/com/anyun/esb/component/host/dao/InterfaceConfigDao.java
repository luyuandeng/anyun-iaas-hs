package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.InterfaceConfigDto;
import com.anyun.exception.DaoException;

/**
 * Created by gp on 16-12-30.
 */
public interface InterfaceConfigDao {
    InterfaceConfigDto selectConfig(String name) throws DaoException;
}
