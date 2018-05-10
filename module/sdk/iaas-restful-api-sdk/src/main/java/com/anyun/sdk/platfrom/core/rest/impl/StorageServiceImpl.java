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

package com.anyun.sdk.platfrom.core.rest.impl;


import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.StorageDto;
import com.anyun.cloud.param.*;
import com.anyun.sdk.platfrom.StorageService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 5/12/16
 */
public class StorageServiceImpl implements StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StorageServiceImpl.class);

    public static final String PATH_CREATE_STORAGE = "/storage/create";
    public static final String PATH_UPDATE_STORAGE = "/storage/update";
    public static final String PATH_QUERY_STORAGE_BY_ID = "/storage/details";
    public static final String PATH_QUERY_STORAGE_ALL = "/storage/allList";
    public static final String PATH_UPDATE_STORAGE_STATE="/storage/modifyState";
    public static final String PATH_DELETE_STORAGE="/storage/deleteStorage";



    @Override
    public StorageDto storageCreate(StorageCreateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        LOGGER.debug("param:" + param.asJson());
        String response = rsClient.put(PATH_CREATE_STORAGE, param.asJson());
        StorageDto requests = ResourceClient.convertToAnyunEntity(StorageDto.class, response);
        return requests;
    }


    @Override
    public StorageDto storageUpdate(StorageUpdateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        LOGGER.debug("param:" + param.asJson());
        String response = rsClient.post(PATH_UPDATE_STORAGE, param.asJson());
        StorageDto requests = ResourceClient.convertToAnyunEntity(StorageDto.class, response);
        return requests;
    }


    @Override
    public StorageDto storageQueryById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_STORAGE_BY_ID + "/" + id, param);
        StorageDto requests = ResourceClient.convertToAnyunEntity(StorageDto.class, response);
        return requests;
    }

    @Override
    public List<StorageDto> storageAllQuery(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_STORAGE_ALL, param);
        List<StorageDto> requests = ResourceClient.convertToListObject(StorageDto.class, response);
        return requests;
    }

    @Override
    public Status<String> storageUpdateState(StorageUpdateStateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        LOGGER.debug("param:" + param.asJson());
        String response = rsClient.post(PATH_UPDATE_STORAGE_STATE, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public Status<String> storageDelete(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.delete(PATH_DELETE_STORAGE + "/" + id, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }


}
