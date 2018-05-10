package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.PlatformDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.PlatformDao;
import com.anyun.exception.DaoException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-10-13.
 */
public class PlatformDaoImpl extends BaseMyBatisDao implements PlatformDao {
    @Override
    public void insertPlatform(PlatformDto platformDto) throws DaoException {
        String sql = "dao.PlatformDao.insertPlatform";
        insert(sql, platformDto);
    }

    @Override
    public void updatePlatform(PlatformDto platformDto) throws DaoException {
        String sql = "dao.PlatformDao.updatePlatform";
        update(sql, platformDto);
    }


    @Override
    public void deletePlatform(String id) throws DaoException {
        String sql = "dao.PlatformDao.deletePlatform";
        Map<String, Object> m = new HashMap<>();
        m.put("id", id);
        delete(sql, m);
    }

    @Override
    public PlatformDto selectPlatformById(String id) throws DaoException {
        String sql = "dao.PlatformDao.selectPlatformById";
        Map<String, Object> m = new HashMap<>();
        m.put("id", id);
        return selectOne(PlatformDto.class, sql, m);
    }

    @Override
    public List<PlatformDto> selectPlatformByStatus(int status) throws DaoException {
        String sql = "dao.PlatformDao.selectPlatformByStatus";
        return selectList(PlatformDto.class, sql, status);
    }

    @Override
    public List<PlatformDto> selectPlatformAll() throws DaoException {
        String sql = "dao.PlatformDao.selectPlatformAll";
        return selectList(PlatformDto.class, sql, null);
    }

    @Override
    public void updatePlatformByStatusAndId(int status, String id) throws DaoException {
        String sql = "dao.PlatformDao.updatePlatformByStatusAndId";
        Map<String, Object> m = new HashMap<>();
        m.put("status", status);
        m.put("id", id);
        update(sql, m);
    }

    @Override
    public void updatePlatformByCondition(int status, String id) throws DaoException {
        String sql = "dao.PlatformDao.updatePlatformByCondition";
        Map<String, Object> m = new HashMap<>();
        m.put("status", status);
        m.put("id", id);
        update(sql, m);
    }

}
