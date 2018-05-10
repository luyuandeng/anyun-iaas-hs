package com.anyun.esb.component.host.dao;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 16-7-12.
 */
public interface  ProjectDao {
    void  insertProject(ProjectDto dto) throws DaoException;

    ProjectDto updateProject(ProjectDto dto) throws DaoException;

    void deleteProjectById(String id) throws  DaoException;

    List<ProjectDto> selectProjectByCondition(String  userUniqueId) throws DaoException;

    ProjectDto selectProjectById(String id) throws DaoException;

    ProjectDto selectProjectDtoByPlatFormNetworkId(String label) throws DaoException;
}
