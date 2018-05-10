package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.InterfaceConfigDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;

import com.anyun.esb.component.host.dao.InterfaceConfigDao;
import com.anyun.exception.DaoException;

/**
 * Created by gp on 16-12-30.
 */
public class InterfaceConfigDaoImpl extends BaseMyBatisDao  implements InterfaceConfigDao {

    @Override
    public InterfaceConfigDto selectConfig(String name) throws DaoException {
        String sql = "dao.InterfaceConfigDao.selectConfig";
        return selectOne(InterfaceConfigDto.class,sql,name);
    }
}
