/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.anyun.esb.component.storage.dao.impl;


import com.anyun.cloud.dto.*;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.storage.dao.BaseMyBatisDao;
import com.anyun.esb.component.storage.dao.StorageDao;
import com.anyun.exception.DaoException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 5/11/16
 */
public class StorageDaoImpl extends BaseMyBatisDao implements StorageDao {
    @Override
    public StorageDto insertStorage(StorageDto storageDto) throws DaoException {
        String sql = "dao.StorageDao.insertStorage";
        String id = StringUtils.uuidGen();
        Date date = new Date();
        storageDto.setId(id);
        storageDto.setCreateDate(date);
        storageDto.setLastModifyDate(date);
        storageDto.setLinkState("alreadyLink");
        storageDto.setAvailableState("usable");
        insert(sql, storageDto);
        return selectStorageById(id);
    }

    @Override
    public StorageDto updateStorage(StorageDto storageDto) throws DaoException {
        String sql = "dao.StorageDao.updateStorage";
        storageDto.setLastModifyDate(new Date());
        update(sql, storageDto);
        return  selectStorageById(storageDto.getId());
    }

    @Override
    public StorageDto selectStorageById(String id) throws DaoException {
        String sql = "dao.StorageDao.selectStorageById";
        return selectOne(StorageDto.class, sql, id);
    }

    @Override
    public StorageDto selectStorageByType(String type) throws DaoException {
        String sql = "dao.StorageDao.selectStorageByType";
        return selectOne(StorageDto.class,sql,type);
    }

    @Override
    public List<StorageDto> selectAllStorage() throws DaoException {
        String sql = "dao.StorageDao.selectAllStorage";
        return selectList(StorageDto.class, sql, new Object());
    }


    @Override
    public ProjectDto selectProjectById(String id) throws DaoException {
        String sql = "dao.StorageDao.selectProjectById";
        return selectOne(ProjectDto.class, sql, id);
    }


    @Override
    public ContainerDto selectContainerById(String container) throws DaoException {
        String sql="dao.StorageDao.selectContainerById";
        return selectOne(ContainerDto.class,sql,container);
    }

    @Override
    public void deleteStorageByType(String type) throws DaoException {
        String sql="dao.StorageDao.deleteStorageByType";
        delete(sql,type);
    }

    @Override
    public void updateStorageState(StorageDto dto) throws DaoException {
        String sql = "dao.StorageDao.updateStorageState";
        dto.setLastModifyDate(new Date());
        update(sql,dto);
    }

    @Override
    public void deleteStorageById(String id) throws DaoException {
        String sql = "dao.StorageDao.deleteStorageById";
        delete(sql,id);
    }


}
