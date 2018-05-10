package com.anyun.esb.component.storage.dao;


import com.anyun.cloud.dto.*;
import com.anyun.exception.DaoException;

import java.util.List;

/**
 * Created by sxt on 16-8-10.
 */
public interface StorageDao {
    StorageDto insertStorage(StorageDto storageDto) throws DaoException;

    StorageDto updateStorage(StorageDto storageDto) throws DaoException;

    StorageDto selectStorageById(String id) throws DaoException;

    StorageDto selectStorageByType(String type) throws DaoException;

    List<StorageDto> selectAllStorage() throws DaoException;

    ProjectDto selectProjectById(String project) throws DaoException;

    ContainerDto selectContainerById(String container) throws DaoException;

    void deleteStorageByType(String type) throws DaoException;

    void updateStorageState(StorageDto dto) throws DaoException;

    void deleteStorageById(String id) throws DaoException;


}
