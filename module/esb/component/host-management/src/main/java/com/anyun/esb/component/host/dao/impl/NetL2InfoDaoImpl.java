package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.NetL2InfoDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.NetL2InfoDao;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 17-4-7.
 */
public class NetL2InfoDaoImpl extends BaseMyBatisDao implements NetL2InfoDao {
    @Override
    public NetL2InfoDto selectNetL2InfById(String id) throws DaoException {
        String sql = "dao.NetL2InfoDao.selectNetL2InfById";
        return selectOne(NetL2InfoDto.class,sql,id);
    }

    @Override
    public List<NetL2InfoDto> selectNetL2ListByType(String type) throws DaoException {
        String sql ="dao.NetL2InfoDao.selectNetL2ListByType";
        return selectList(NetL2InfoDto.class,sql,type);
    }

    @Override
    public void deleteNetL2ById(String id) throws DaoException {
        String sql = "dao.NetL2InfoDao.deleteNetL2ById";
        delete(sql,id);
    }

    @Override
    public NetL2InfoDto createNetL2(NetL2InfoDto netL2InfoDto) throws DaoException {
        String sql = "dao.NetL2InfoDao.createNetL2";
        insert(sql,netL2InfoDto);
        return selectNetL2InfById(netL2InfoDto.getId());
    }

    @Override
    public NetL2InfoDto updateNetL2(NetL2InfoDto netL2InfoDto) throws DaoException {
        String sql = "dao.NetL2InfoDao.updateNetL2";
        update(sql,netL2InfoDto);
        return selectNetL2InfById(netL2InfoDto.getId());
    }
}
