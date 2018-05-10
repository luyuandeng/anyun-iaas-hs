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

package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.StorageDto;
import com.anyun.cloud.param.*;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 5/12/16
 */
public interface StorageService {
    /**
     * 创建存储
     *
     * @param
     * @return Status<String>
     */
    StorageDto storageCreate(StorageCreateParam param) throws RestfulApiException;

    /**
     * 修改存储
     *
     * @param
     * @return Status<String>
     */
    StorageDto storageUpdate(StorageUpdateParam param) throws RestfulApiException;

    /**
     * 查询存储由id
     *
     * @param id
     * @return StorageDto
     * @throws RestfulApiException
     * @parm userUniqueId
     */
    StorageDto storageQueryById(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 查询所有存储
     *
     * @param userUniqueId
     * @return List<Storage>
     * @throws RestfulApiException
     */
    List<StorageDto> storageAllQuery(String userUniqueId) throws RestfulApiException;



    /**
     * 更新存储状态
     * @param param
     * @return
     * @throws RestfulApiException
     */
    Status<String> storageUpdateState(StorageUpdateStateParam param) throws RestfulApiException;

    /**
     * 删除存储
     * @param id
     * @param userUniqueId
     * @return
     * @throws RestfulApiException
     */
    Status<String> storageDelete(String id, String userUniqueId) throws RestfulApiException;



}
