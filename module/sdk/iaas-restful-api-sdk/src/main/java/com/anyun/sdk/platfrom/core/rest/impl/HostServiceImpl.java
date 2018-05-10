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
import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.HostApprovalParam;
import com.anyun.cloud.param.HostCreateParam;
import com.anyun.cloud.param.HostStatusUpdateParam;
import com.anyun.cloud.param.QpidQueueParam;
import com.anyun.sdk.platfrom.HostService;
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
 * @date 5/6/16
 */
public class HostServiceImpl implements HostService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostServiceImpl.class);
    public static final String PATH_HOST_APPROVAL = "/host/approval";
    public static final String PATH_HOST_QUERY_CONDITION = "/host";
    public static final String PATH_HOST_CPU_QUERY = "/host/cpu";
    public static final String PATH_HOST_MEMORY_QUERY = "/host/memory";
    public static final String PATH_HOST_DETAILS = "/host/details";
    public static final String PATH_HOST_ALL_LIST = "/host/allList";
    public static final String PATH_HOST_REALTIME_LIST = "/host/realTimeList";
    public static final String PATH_HOST_CREATE = "/host/hostCreate";
    public static final String PATH_HOST_STATUS_UPDATE = "/host/hostStatusUpdate";
    public static final String PATH_HOST_DELETE = "/host/delete";
    public static final String PATH_QPID_QUEUE = "/host/getQueueFromQpid";

    @Override
    public Status<String> approval(HostApprovalParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_HOST_APPROVAL, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public List<HostBaseInfoDto> getHostBaseInfoByStatus(int status, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("status", status);
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_HOST_QUERY_CONDITION, params);
        List<HostBaseInfoDto> requests = ResourceClient.convertToListObject(HostBaseInfoDto.class, response);
        return requests;
    }

    @Override
    public HostCpuInfoDto hostCpuInfoQuery(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_HOST_CPU_QUERY, params);
        HostCpuInfoDto requests = ResourceClient.convertToAnyunEntity(HostCpuInfoDto.class, response);
        return requests;
    }

    @Override
    public List<HostMemoryInfoDto> hostMemoryInfoQuery(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_HOST_MEMORY_QUERY, param);
        List<HostMemoryInfoDto> requests = ResourceClient.convertToListObject(HostMemoryInfoDto.class, response);
        return requests;
    }

    @Override
    public List<HostBaseInfoDto> queryAllHostInfo(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_HOST_ALL_LIST, params);
        List<HostBaseInfoDto> requests = ResourceClient.convertToListObject(HostBaseInfoDto.class, response);
        return requests;
    }

    @Override
    public HostBaseInfoDto queryHostInfoById(String id, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_HOST_DETAILS + "/" + id, params);
        HostBaseInfoDto requests = ResourceClient.convertToAnyunEntity(HostBaseInfoDto.class, response);
        return requests;
    }

    @Override
    public List<HostRealTimeDto> hostRealTimeInfoQuery(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_HOST_REALTIME_LIST, params);
        List<HostRealTimeDto> requests = ResourceClient.convertToListObject(HostRealTimeDto.class, response);
        return requests;
    }

    @Override
    public HostTailDto hostCreate(HostCreateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.put(PATH_HOST_CREATE, param.asJson());
        HostTailDto requests = ResourceClient.convertToAnyunEntity(HostTailDto.class, response);
        return requests;
    }

    @Override
    public Status<String> hostUpdateStatus(HostStatusUpdateParam param) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        String response = rsClient.post(PATH_HOST_STATUS_UPDATE, param.asJson());
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public Status<String> hostDelete(String ip, String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        LOGGER.debug("PARAMS:" + param);
        String response = rsClient.delete(PATH_HOST_DELETE + "/" + ip, param);
        Status<String> requests = ResourceClient.convertToAnyunEntity(Status.class, response);
        return requests;
    }

    @Override
    public List<QpidQueueDto> getQueueFromQpid(QpidQueueParam param) {
        ResourceClient rsClient=AnyunSdkClientFactory.getFactory().getResourceClient();
        String response=rsClient.post(PATH_QPID_QUEUE,param.asJson());
        List<QpidQueueDto> requests=ResourceClient.convertToListObject(QpidQueueDto.class,response);
        return requests;
    }
}
