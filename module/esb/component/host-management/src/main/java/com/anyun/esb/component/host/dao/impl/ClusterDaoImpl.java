package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.ClusterDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.ClusterDao;
import com.anyun.exception.DaoException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 17-3-28.
 */
public class ClusterDaoImpl extends BaseMyBatisDao implements ClusterDao {
    @Override
    public List<ClusterDto> queryAllCluster() {
        String sql = "dao.ClusterDao.queryAllCluster";
        List<ClusterDto> list = selectList(ClusterDto.class, sql, "");
        return list;
    }

    @Override
    public List<ClusterDto> selectClusterByStatus(String status) {
        String sql = "dao.ClusterDao.selectClusterByStatus";
        Map<String,Object>   m=new HashMap<>();
        m.put("status",status);
        List<ClusterDto> list = selectList(ClusterDto.class, sql, m);
        return list;
    }

    @Override
    public List<ClusterDto> selectClusterByType(String type) {
        String sql = "dao.ClusterDao.selectClusterByType";
        List<ClusterDto> list = selectList(ClusterDto.class, sql, type);
        return list;
    }

    @Override
    public List<ClusterDto> selectClusterByTypeAndStatus(String type, String status) {
        String sql = "dao.ClusterDao.selectClusterByTypeAndStatus";
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("status", status);
        return selectList(ClusterDto.class, sql, params);
    }

    @Override
    public ClusterDto selectClusterById(String id) {
        String sql = "dao.ClusterDao.selectClusterById";
        return selectOne(ClusterDto.class, sql, id);
    }

    @Override
    public ClusterDto createCluster(ClusterDto dto) throws DaoException {
        String sql = "dao.ClusterDao.insertCluster";
        insert(sql, dto);
        return selectClusterById(dto.getId());
    }

    @Override
    public ClusterDto updateCluster(ClusterDto dto) throws DaoException {
        String sql = "dao.ClusterDao.updateCluster";
        update(sql, dto);
        return selectClusterById(dto.getId());
    }

    @Override
    public void deleteCluster(String id) throws DaoException {
        String sql = "dao.ClusterDao.deleteCluster";
        delete(sql, id);
    }

    @Override
    public void changeClusterStatus(ClusterDto dto) throws DaoException {
        String sql = "dao.ClusterDao.changeClusterStatus";
        update(sql, dto);
    }
}
