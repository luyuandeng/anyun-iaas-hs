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

package com.anyun.esb.component.host.dao;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.HostCreateParam;
import com.anyun.cloud.param.HostStatusUpdateParam;
import com.anyun.exception.DaoException;

import java.util.List;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/26/16
 */
public interface HostBaseInfoDao {

    /**
     * @param id
     * @return
     */
    HostBaseInfoDto selectById(String id) throws DaoException;

    /**
     * @param id
     * @throws DaoException
     */
    void insertHostRequest(String id) throws DaoException;

    /**
     * @param status
     * @return
     */
    List<HostBaseInfoDto> selectByStatus(int status) throws DaoException;

    /**
     * @param host
     * @param ip
     * @throws DaoException
     */
    void insertHostManagementIp(String host, String ip) throws DaoException;

    /**
     * @param host
     * @param ip
     * @throws DaoException
     */
    void updateHostManagementIp(String host, String ip) throws DaoException;

    /**
     * @param dockerHostInfoDto
     * @throws DaoException
     */
    void insertDockerHostInfo(DockerHostInfoDto dockerHostInfoDto) throws DaoException;

    /**
     * @param dockerHostInfoDto
     * @throws DaoException
     */
    void updateDockerHostInfo(DockerHostInfoDto dockerHostInfoDto) throws DaoException;

    /**
     * @param hostBaseInfoDto
     * @throws DaoException
     */
    void updateHostBaseInfo(HostBaseInfoDto hostBaseInfoDto) throws DaoException;

    /**
     * @param hostBaseInfoDto
     * @throws DaoException
     */
    void approval(HostBaseInfoDto hostBaseInfoDto) throws DaoException;

    /**
     *
     * @param managementIp
     * @param extInfoDto
     * @throws DaoException
     */
    void insertHostExtInfo(String managementIp, HostExtInfoDto extInfoDto) throws DaoException;

    /**
     *
     * @param serialNumber
     * @return
     * @throws DaoException
     */
    DockerHostInfoDto selectDockerHostDto(String serialNumber)  throws DaoException;

    /**
     *
     * @param ip
     * @return
     * @throws DaoException
     */
    HostManagementIpInfoDto selectManagementIpDto(String ip) throws DaoException;


    /**
     *
     * @param host
     * @return HostManagementIpInfoDto
     * @throws DaoException
     */
    HostManagementIpInfoDto selectManagementIpDtoByHost(String host) throws DaoException;


    /**
     *
     * @param id
     * @param status
     * @throws DaoException
     */
    void updateDockerHostStatus(String id, int status) throws DaoException;

    /**
     *  @return cpuFamily
     *  @throws DaoException
     */
    List<String>    selectHostAllCpuFamily()   throws DaoException;

    /**
     * @return  cpuCoreLimit
     * @throws DaoException
     */
    List<Integer>   selectHostAllCpuCoreLimit() throws DaoException;


    /**
     * @param  cpuFamily
     * @param  cpuCoreLimit
     * @param  memoryLimit
     * @param  cpuPresentMode
     * @return  ip
     * @throws DaoException
     */
    List<String>    selectHostListByCondition(String cpuFamily, Integer cpuCoreLimit, Integer memoryLimit, Integer cpuPresentMode) throws DaoException;


    /**
     * @param  ip
     * @return  HostId
     * @throws DaoException
     */
    String    selectHostIdByIp(String ip) throws DaoException;

    /**
     * @param
     * @return    List<HostMemoryInfoDto>
     * @throws DaoException
     */
    List<HostMemoryInfoDto> selectHostMemoryInfo() throws DaoException;


    /**
     * @param
     * @return    List<HostBaseInfoDto>
     * @throws    DaoException
     */
    List<HostBaseInfoDto> selectAllHostInfo() throws DaoException;


    /**
     * @param
     * @return    HostBaseInfoDto
     * @throws    DaoException
     */
    HostBaseInfoDto selectHostInfoById(String id) throws  DaoException;

    /**
     * @param     card
     * @throws    DaoException
     */
    void  insertHostNetworkCard(HostNetworkCardDto card) throws  DaoException;

    /**
     * @param    id
     * @param    host
     * @return   HostNetworkCardDto
     * @throws   DaoException
     */
    HostNetworkCardDto selectHostNetworkCardByHostAndMac(String id, String host);

    /**
     * 初始化宿主机结束后修改宿主机信息
     * @param param
     * @throws DaoException
     */
    void updateHostTail(HostCreateParam param) throws DaoException;

    /**
     * 根据宿主机ip查询宿主机详情
     * @param ip
     * @return
     * @throws DaoException
     */
    HostTailDto selectHostInfoByDescription(String ip) throws DaoException;

    /**
     * 根据宿主机ip修改宿主机状态
     * @throws DaoException
     */
    void updateHostByIp(HostStatusUpdateParam param) throws DaoException;

    void deleteFromHostInfoManagementIp(String id) throws DaoException;

    void deleteFromHostInfoExt(String id) throws DaoException;

    void deleteFromDockerHostInfo(String id) throws DaoException;

    void deleteFromHostNetworkCard(String id) throws DaoException;

    void deleteFromHostInfoBase(String id) throws DaoException;

    List<HostBaseInfoDto> selectHostByCluster(String id) throws DaoException;

    String selectLabelByType();

    HostExtInfoDto selectHExtDtoByHostId(String hostId) throws DaoException;
}
