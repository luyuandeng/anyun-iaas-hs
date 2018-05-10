package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.DatabaseContainerDto;
import com.anyun.cloud.dto.DatabaseDto;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 7/4/17.
 */
public interface DatabaseDao  {
    DatabaseDto selectDatabaseDtoById(String id, String userUniqueId) throws DaoException;

    void deleteById(String id) throws DaoException;

    List<DatabaseContainerDto> selectDatabaseContainerDtoListByDatabaseId(String databaseId) throws DaoException;

    void deleteDbContainerByContainerId(String containerId) throws DaoException;

    void deleteDbContainerByDatabaseId(String databaseId)throws DaoException;

    DatabaseDto insertDatabase(DatabaseDto dDto)throws DaoException;

    DatabaseContainerDto  insertDbContainer(DatabaseContainerDto dbC) throws DaoException;

    List<DatabaseDto> selectDatabaseDtoListByProjectId(String projectId) throws DaoException;
}
