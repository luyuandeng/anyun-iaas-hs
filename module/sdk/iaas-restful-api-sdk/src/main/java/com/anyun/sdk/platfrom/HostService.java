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
import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.HostApprovalParam;
import com.anyun.cloud.param.HostCreateParam;
import com.anyun.cloud.param.HostStatusUpdateParam;
import com.anyun.cloud.param.QpidQueueParam;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 5/6/16
 */
public interface  HostService {
    /**
     * 宿主机审批
     *
     * @param param
     * @return Status<String>
     * @throws RestfulApiException
     */
    Status<String> approval(HostApprovalParam param) throws RestfulApiException;

    /**
     * 根据宿主机状态查找宿主机
     *
     * @param status
     * @param userUniqueId
     * @return List<HostBaseInfoDto>
     * @throws RestfulApiException
     */
    List<HostBaseInfoDto> getHostBaseInfoByStatus(int status, String userUniqueId) throws RestfulApiException;


    /**
     * 获取 CPU 信息
     *
     * @param userUniqueId
     * @return HostCpuInfoDto
     * @throws RestfulApiException
     */
    HostCpuInfoDto hostCpuInfoQuery(String userUniqueId) throws RestfulApiException;


    /**
     * 获取内存信息
     *
     * @return List<HostMemoryInfoDto>
     * @throws RestfulApiException
     * @oaram userUniqueId
     */
    List<HostMemoryInfoDto> hostMemoryInfoQuery(String userUniqueId) throws RestfulApiException;


    /**
     * 获取所有宿主机信息
     *
     * @param userUniqueId
     * @return List<HostBaseInfoDto>
     * @throws RestfulApiException
     */
    List<HostBaseInfoDto> queryAllHostInfo(String userUniqueId) throws RestfulApiException;

    /**
     * 很据id  查询宿主机信息
     *
     * @param id
     * @param userUniqueId
     * @return HostBaseInfoDto
     * @throws RestfulApiException
     */
    HostBaseInfoDto queryHostInfoById(String id, String userUniqueId) throws RestfulApiException;

    /**
     * 查询宿主机实时数据
     *
     * @param userUniqueId
     * @return List<HostAndIpDto>
     * @throws RestfulApiException
     */
    List<HostRealTimeDto> hostRealTimeInfoQuery(String userUniqueId) throws RestfulApiException;

    HostTailDto hostCreate(HostCreateParam param) throws RestfulApiException;

    Status<String> hostUpdateStatus(HostStatusUpdateParam param) throws RestfulApiException;

    Status<String> hostDelete(String ip,String userUniqueId) throws RestfulApiException;

    List<QpidQueueDto> getQueueFromQpid(QpidQueueParam param);
}

