package com.anyun.esb.component.storage.service.storage.impl;

import com.anyun.cloud.dto.StorageDto;
import com.anyun.cloud.dto.StorageInfo;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.storage.dao.StorageDao;

import com.anyun.esb.component.storage.dao.impl.StorageDaoImpl;

import com.anyun.esb.component.storage.service.storage.StorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by sxt on 16-8-10.
 */
public class StorageServiceImpl implements StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);
    public StorageDao storageDao = new StorageDaoImpl();

    //创建存储  在每台宿主机分别创建存储
    @Override
    public StorageDto storageCreate(StorageCreateParam param) throws Exception {
        if (!param.getType().equals("gluster"))
            throw new UnsupportedOperationException(param.getType());
        if (!param.getPurpose().equals("docker.volume"))
            throw new UnsupportedOperationException(param.getPurpose());
        StorageDto storage = storageDao.selectStorageByType(param.getType());
        if (storage == null) {
            StorageDto storageDto = new StorageDto();
            storageDto.setName(param.getName());
            storageDto.setDescr(param.getDescr());
            storageDto.setPurpose(param.getPurpose());
            storageDto.setType(param.getType());
            storageDto.setFilesystem(param.getFilesystem());
            return storageDao.insertStorage(storageDto);
        }
        return storage;
    }

    //查询所有存储
    @Override
    public List<StorageDto> storageAllQuery() throws Exception {
        return storageDao.selectAllStorage();
    }

    //修改存储
    @Override
    public StorageDto storageUpdate(StorageUpdateParam param) throws Exception {
        if (storageQueryById(param.getId()) == null)
            throw new Exception("This storage :[" + param.getId() + "]does not exists");
        StorageDto storageDto = new StorageDto();
        storageDto.setId(param.getId());
        storageDto.setName(param.getName());
        storageDto.setDescr(param.getDescr());
        return storageDao.updateStorage(storageDto);
    }

    //根据id查询存储
    @Override
    public StorageDto storageQueryById(String id) throws Exception {
        return storageDao.selectStorageById(id);
    }

    //根据类型查询存储
    @Override
    public StorageDto storageQueryByType(String type) throws Exception {
        StorageDto storageDto = storageDao.selectStorageByType(type);
        if (storageDto == null || StringUtils.isEmpty(storageDto.getId()))
            return null;
        return storageDto;
    }

    //更新存储状态
    @Override
    public void UpdateStorageState(StorageUpdateStateParam param) throws Exception {
        StorageDto dto = new StorageDto();
        dto.setId(param.getId());
        dto.setAvailableState(param.getAvailableState());
        storageDao.updateStorageState(dto);
    }


    //删除存储
    @Override
    public void deleteStorageById(String id) throws Exception {
        storageDao.deleteStorageById(id);
    }


    @Override
    public StorageInfo storageRealTimeDataQuery() throws Exception {


        return new StorageInfo();
    }
}
