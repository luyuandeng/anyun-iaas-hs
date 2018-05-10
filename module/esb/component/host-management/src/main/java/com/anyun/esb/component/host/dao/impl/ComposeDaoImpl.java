package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.ComposeDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.ComposeDao;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 16-12-6.
 */
public class ComposeDaoImpl extends BaseMyBatisDao implements ComposeDao {
    @Override
    public void deleteComposeById(String id) throws DaoException {
        String sql = "dao.ComposeDao.deleteComposeById";
        delete(sql, id);
    }

    @Override
    public List<ComposeDto> selectComposeByUserUniqueId(String userUniqueId) throws DaoException {
        String sql = "dao.ComposeDao.selectComposeByUserUniqueId";
        return selectList(ComposeDto.class, sql, userUniqueId);
    }

    @Override
    public ComposeDto selectComposeById(String id) throws DaoException {
        String sql = "dao.ComposeDao.selectComposeById";
        return selectOne(ComposeDto.class, sql, id);
    }

    @Override
    public void insertCompose(ComposeDto composeDto) throws DaoException {
        String sql = "dao.ComposeDao.insertCompose";
        insert(sql, composeDto);
    }
}
