package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.exception.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by sxt on 16-7-12.
 */
public class ProjectDaoImpl extends BaseMyBatisDao implements ProjectDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectDaoImpl.class);

    @Override
    public void insertProject(ProjectDto dto) throws DaoException {
        Date date = new Date();
        dto.setCreateDate(date);
        dto.setLastModifyDate(date);
        LOGGER.debug("dto:[{}]",dto.asJson());
        String sql = "dao.ProjectDao.insertProject";
        insert(sql, dto);
    }

    @Override
    public ProjectDto updateProject(ProjectDto dto) throws DaoException {
        dto.setLastModifyDate(new Date());
        String sql = "dao.ProjectDao.updateProject";
        update(sql, dto);
        String SQL = "dao.ProjectDao.selectProjectById";
        return selectOne(ProjectDto.class, SQL, dto.getId());
    }

    @Override
    public void deleteProjectById(String id) throws DaoException {
        String sql = "dao.ProjectDao.deleteProjectById";
        delete(sql, id);
    }

    @Override
    public List<ProjectDto> selectProjectByCondition(String userUniqueId) throws DaoException {
        String sql = "dao.ProjectDao.selectProjectByCondition";
        List<ProjectDto> list = selectList(ProjectDto.class, sql, userUniqueId);
        return list;
    }

    @Override
    public ProjectDto selectProjectById(String id) throws DaoException {
        String sql = "dao.ProjectDao.selectProjectById";
        return selectOne(ProjectDto.class, sql, id);
    }

    @Override
    public ProjectDto selectProjectDtoByPlatFormNetworkId(String label) throws DaoException {
        String sql="dao.ProjectDao.selectProjectDtoByPlatFormNetworkId";
        return selectOne(ProjectDto.class,sql,label);
    }
}
