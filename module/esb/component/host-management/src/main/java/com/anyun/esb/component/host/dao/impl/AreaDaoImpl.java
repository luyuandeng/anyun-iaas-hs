package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.AreaDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.param.Conditions;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.dao.AreaDao;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.exception.DaoException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 17-3-23.
 */
public class AreaDaoImpl extends BaseMyBatisDao implements AreaDao {
    @Override
    public List<AreaDto> queryAllArea() {
        String sql = "dao.AreaDao.queryAllArea";
        List<AreaDto> list = selectList(AreaDto.class, sql, "");
        return list;
    }

    @Override
    public List<AreaDto> selectAreaByStatus(String status) {
        String sql = "dao.AreaDao.selectAreaByStatus";
        List<AreaDto> list = selectList(AreaDto.class, sql, status);
        return list;
    }

    @Override
    public List<AreaDto> selectAreaByType(String type) {
        String sql = "dao.AreaDao.selectAreaByType";
        List<AreaDto> list = selectList(AreaDto.class, sql, type);
        return list;
    }

    @Override
    public List<AreaDto> selectAreaByTypeAndStatus(String type, String status) {
        String sql = "dao.AreaDao.selectAreaByTypeAndStatus";
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("status", status);
        return selectList(AreaDto.class, sql, params);
    }

    @Override
    public void create(AreaDto dto) throws DaoException {
        String sql = "dao.AreaDao.createArea";
        insert(sql, dto);
    }

    @Override
    public void update(AreaDto dto) throws DaoException {
        String sql = "dao.AreaDao.updateArea";
        update(sql, dto);
    }

    @Override
    public AreaDto queryById(String id) throws DaoException {
        String sql = "dao.AreaDao.selectAreaById";
        return selectOne(AreaDto.class, sql, id);
    }

    @Override
    public void deleteArea(String id) throws DaoException {
        String sql = "dao.AreaDao.deleteAreaById";
        delete(sql, id);
    }

    @Override
    public void changeAreaStatus(AreaDto dto) throws DaoException {
        String sql = "dao.AreaDao.changeAreaStatus";
        update(sql, dto);
    }
}
