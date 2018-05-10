package com.anyun.esb.component.storage.service.storage;

import com.anyun.cloud.dto.StorageDto;
import com.anyun.cloud.dto.StorageInfo;
import com.anyun.cloud.param.*;

import java.util.List;

/**
 * Created by sxt on 16-8-10.
 */
public interface StorageService {
    /**
     * 创建存储
     *
     * @param param
     * @return
     */
    StorageDto storageCreate(StorageCreateParam param) throws Exception;

    /**
     * 修改存储
     *
     * @param param
     * @return
     */
    StorageDto storageUpdate(StorageUpdateParam param) throws Exception;

    /**
     * 根据id查询存储
     *
     * @param id
     * @return storageDto
     */
    StorageDto storageQueryById(String id) throws Exception;

    /**
     * 根据类型查询存储
     *
     * @param type
     * @return storageDto
     */
    StorageDto storageQueryByType(String type) throws Exception;

    /**
     * 查询所有存储
     *
     * @param
     * @return dtoList
     */
    List<StorageDto> storageAllQuery() throws Exception;

    /**
     * 更改状态
     *
     * @param param
     */
    void UpdateStorageState(StorageUpdateStateParam param) throws Exception;

    /**
     * 删除存储
     *
     * @param id
     * @throws Exception
     */
    void deleteStorageById(String id) throws Exception;


    /**
     * 存储的实时数据查询
     *
     * @param
     * @return storageInfo
     */
    StorageInfo storageRealTimeDataQuery() throws Exception;
}
