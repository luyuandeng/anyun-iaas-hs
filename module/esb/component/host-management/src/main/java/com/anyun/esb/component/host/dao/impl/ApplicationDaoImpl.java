package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.ApplicationInfoDto;
import com.anyun.cloud.dto.ApplicationInfoLoadDto;
import com.anyun.esb.component.host.dao.ApplicationDao;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.exception.DaoException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sxt on 16-10-25.
 */
public class ApplicationDaoImpl extends BaseMyBatisDao implements ApplicationDao {
    @Override
    public void applicationDeleteById(String id) throws DaoException {
        String sql = "dao.ApplicationDao.applicationDeleteById";
        delete(sql, id);
    }

    @Override
    public ApplicationInfoDto applicationSelectById(String id) throws DaoException {
        String sql = "dao.ApplicationDao.selectApplicationById";
        return selectOne(ApplicationInfoDto.class, sql, id);
    }

    @Override
    public List<ApplicationInfoDto> applicationSelectByProject(String project) throws DaoException {
        String sql = "dao.ApplicationDao.applicationSelectByProject";
        return selectList(ApplicationInfoDto.class, sql, project);
    }

    @Override
    public ApplicationInfoDto applicationInsert(ApplicationInfoDto applicationInfoDto) throws DaoException {
        String sql = "dao.ApplicationDao.InsertApplicationInsert";
        insert(sql, applicationInfoDto);
        return applicationSelectById(applicationInfoDto.getId());
    }

    @Override
    public ApplicationInfoLoadDto loadInsert(ApplicationInfoLoadDto dto) throws DaoException {
        String sql = "dao.ApplicationDao.insertLoad";
        insert(sql, dto);
        return   selectLoadDtoById(dto.getLoadContainer());
    }

    @Override
    public List<ApplicationInfoLoadDto> selectLoadDtoByApplication(String id) throws DaoException {
        String sql = "dao.ApplicationDao.selectLoadDtoByApplication";
        return selectList(ApplicationInfoLoadDto.class, sql, id);
    }

    @Override
    public void deleteLoadByApplication(String id) throws DaoException {
        String sql = "dao.ApplicationDao.deleteLoadByApplication";
        delete(sql, id);
    }

    @Override
    public ApplicationInfoDto selectApplicationDtoByTemplateContainer(String container) throws DaoException {
        String sql = "dao.ApplicationDao.selectApplicationDtoByTemplateContainer";
        return selectOne(ApplicationInfoDto.class, sql, container);
    }

    @Override
    public ApplicationInfoLoadDto selectLoadDtoById(String id) throws DaoException {
        String sql="dao.ApplicationDao.selectLoadDaoById";
        return selectOne(ApplicationInfoLoadDto.class,sql,id);
    }

    @Override
    public void updateLoadsTotal(String id, int total) throws DaoException {
        String sql="dao.ApplicationDao.updateLoadsTotal";
        Map<String,Object> m=new HashMap<>();
        m.put("id",id);
        m.put("loadsTotal",total);
        update(sql,m);
    }

    @Override
    public void deleteLoad(String id) throws DaoException {
        String  sql="dao.ApplicationDao.deleteLoad";
        delete(sql,id);
    }
}
