package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.DatabaseContainerDto;
import com.anyun.cloud.dto.DatabaseDto;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.DatabaseDao;
import com.anyun.exception.DaoException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 7/4/17.
 */
public class DatabaseDaoImpl  extends BaseMyBatisDao implements DatabaseDao {
    @Override
    public DatabaseDto selectDatabaseDtoById(String id, String userUniqueId) throws DaoException {
        String sql="dao.DatabaseDao.selectDatabaseDtoById";
        Map<String,Object> param=new HashMap<>();
        param.put("id",id);
        param.put("userUniqueId",userUniqueId);
        return selectOne(DatabaseDto.class,sql,param);
    }

    @Override
    public void deleteById(String id) throws DaoException {
        String sql="dao.DatabaseDao.deleteById";
        Map<String,Object>  param=new HashMap<>();
        param.put("id",id);
        delete(sql,param);
    }

    @Override
    public List<DatabaseContainerDto> selectDatabaseContainerDtoListByDatabaseId(String databaseId) throws DaoException {
       String sql="dao.DatabaseDao.selectDatabaseContainerDtoListByDatabaseId";
       Map<String,Object> param=new HashMap<>();
       param.put("databaseId",databaseId);
       return selectList(DatabaseContainerDto.class,sql,param);
    }

    @Override
    public void deleteDbContainerByContainerId(String containerId) throws DaoException {
        String sql="dao.DatabaseDao.deleteDbContainerByContainerId";
        Map<String,Object>  param=new HashMap<>();
        param.put("containerId",containerId);
        delete(sql,param);
    }

    @Override
    public void deleteDbContainerByDatabaseId(String databaseId) throws DaoException {
        String sql="dao.DatabaseDao.deleteDbContainerByDatabaseId";
        Map<String,Object>  param =new HashMap<>();
        param.put("databaseId",databaseId);
        delete(sql,param);
    }

    @Override
    public DatabaseDto insertDatabase(DatabaseDto dDto) throws DaoException {
        String sql="dao.DatabaseDao.insertDatabase";
        insert(sql,dDto);
        return selectDatabaseDtoById(dDto.getId(),"");
    }

    @Override
    public DatabaseContainerDto insertDbContainer(DatabaseContainerDto dbC) throws DaoException {
        String sql="dao.DatabaseDao.insertDbContainer";
        insert(sql,dbC);
        return dbC;
    }

    @Override
    public List<DatabaseDto> selectDatabaseDtoListByProjectId(String projectId) throws DaoException {
        String sql="dao.DatabaseDao.selectDatabaseDtoListByProjectId";
        Map<String,Object>  param=new HashMap<>();
        param.put("projectId",projectId);
        return selectList(DatabaseDto.class,sql,param);
    }
}
