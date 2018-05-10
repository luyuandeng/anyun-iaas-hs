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

package com.anyun.esb.component.host.dao.impl;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.HostCreateParam;
import com.anyun.cloud.param.HostStatusUpdateParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.dao.BaseMyBatisDao;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.exception.DaoException;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/26/16
 */
public class HostBaseInfoDaoImpl extends BaseMyBatisDao implements HostBaseInfoDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostBaseInfoDaoImpl.class);

    @Override
    public HostBaseInfoDto selectById(String id) {
        return selectById(HostBaseInfoDto.class, "dao.HostBaseInfoDao", id);
    }

    @Override
    public void insertHostRequest(String id) {
        String sql = "dao.HostBaseInfoDao.insert";
        HostBaseInfoDto hostBaseInfoDto = new HostBaseInfoDto();
        Date date = new Date();
        hostBaseInfoDto.setCreateDate(date);
        hostBaseInfoDto.setDescript("");
        hostBaseInfoDto.setId(id);
        hostBaseInfoDto.setLastModify(date);
        hostBaseInfoDto.setStatus(9);
        insert(sql, hostBaseInfoDto);
    }

    @Override
    public List<HostBaseInfoDto> selectByStatus(int status) {
        String sql = "dao.HostBaseInfoDao.selectByStatus";
        return selectList(HostBaseInfoDto.class, sql, status);
    }

    @Override
    public void insertHostManagementIp(String host, String ip) throws DaoException {
        String sql = "dao.HostBaseInfoDao.insertHostManagementIp";
        Date date = new Date();
        Map<String, Object> params = new HashMap();
        params.put("host", host);
        params.put("ip", ip);
        params.put("createDate", date);
        params.put("lastModify", date);
        params.put("status", 1);
        insert(sql, params);
    }


    @Override
    public void updateHostManagementIp(String host, String ip) throws DaoException {
        String sql = "dao.HostBaseInfoDao.updateHostManagementIp";
        Date date = new Date();
        Map<String, Object> params = new HashMap();
        params.put("host", host);
        params.put("ip", ip);
        params.put("lastModify", date);
        update(sql, params);
    }

    @Override
    public void insertDockerHostInfo(DockerHostInfoDto dockerHostInfoDto) throws DaoException {
        String sql = "dao.HostBaseInfoDao.insertDockerHostInfo";
        Date date = new Date();
        dockerHostInfoDto.setCreateDate(date);
        dockerHostInfoDto.setLastModifyDate(date);
        insert(sql, dockerHostInfoDto);
    }

    @Override
    public void updateDockerHostInfo(DockerHostInfoDto dockerHostInfoDto) throws DaoException {
        String sql = "dao.HostBaseInfoDao.updateDockerHostInfo";
        Date date = new Date();
        dockerHostInfoDto.setLastModifyDate(date);
        update(sql, dockerHostInfoDto);
    }

    @Override
    public void updateHostBaseInfo(HostBaseInfoDto hostBaseInfoDto) throws DaoException {

    }

    @Override
    public void approval(HostBaseInfoDto hostBaseInfoDto) throws DaoException {
        Date createDate = new Date();
        String sql1 = "dao.HostBaseInfoDao.insertApprovalInfo";
        String sql2 = "dao.HostBaseInfoDao.updateHostApprovalInfo";
        HostApprovalDto hostApprovalDto = hostBaseInfoDto.getHostApprovalDto();
        hostApprovalDto.setCreateDate(createDate);
        hostApprovalDto.setLastModifyDate(createDate);
        hostBaseInfoDto.setLastModify(createDate);
        hostApprovalDto.setHost(hostBaseInfoDto.getId());
        SqlSession sqlSession = getBatchSqlSession();
        try {
            sqlSession.insert(sql1, hostApprovalDto);
            sqlSession.update(sql2, hostBaseInfoDto);
            sqlSession.commit();
        } catch (Exception ex) {
            sqlSession.rollback();
            throw new DaoException(0x00000021, ex);
        }
    }

    @Override
    public void insertHostExtInfo(String managementIp, HostExtInfoDto extInfoDto) throws DaoException {
        Date createDate = new Date();
        extInfoDto.setCreateDate(createDate);
        extInfoDto.setLastModifyDate(createDate);
        HostBaseInfoDto hostBaseInfoDto = new HostBaseInfoDto();
        hostBaseInfoDto.setCreateDate(createDate);
        hostBaseInfoDto.setDescript(managementIp);
        hostBaseInfoDto.setId(extInfoDto.getHostId());
        hostBaseInfoDto.setLastModify(createDate);
        hostBaseInfoDto.setStatus(1);
        Map<String, Object> params = new HashMap();
        params.put("host", extInfoDto.getHostId());
        params.put("ip", managementIp);
        params.put("createDate", createDate);
        params.put("lastModify", createDate);
        params.put("status", 1);

        String sql1 = "dao.HostBaseInfoDao.insert";
        String sql2 = "dao.HostBaseInfoDao.insertHostExtInfo";
        String sql3 = "dao.HostBaseInfoDao.insertHostManagementIp";
        SqlSession sqlSession = getBatchSqlSession();
        try {
            sqlSession.insert(sql1, hostBaseInfoDto);
            sqlSession.insert(sql2, extInfoDto);
            sqlSession.insert(sql3, params);
            sqlSession.commit();
        } catch (Exception ex) {
            sqlSession.rollback();
            throw new DaoException(0x00000021, ex);
        }
    }

    @Override
    public DockerHostInfoDto selectDockerHostDto(String serialNumber) {
        String sql = "dao.HostBaseInfoDao.selectDockerInfoById";
        return selectOne(DockerHostInfoDto.class, sql, serialNumber);
    }

    @Override
    public HostManagementIpInfoDto selectManagementIpDto(String ip) {
        String sql = "dao.HostBaseInfoDao.selectManagementIpDto";
        return selectOne(HostManagementIpInfoDto.class, sql, ip);
    }

    @Override
    public HostManagementIpInfoDto selectManagementIpDtoByHost(String host) throws DaoException {
        String sql = "dao.HostBaseInfoDao.selectManagementIpDtoByHost";
        return selectOne(HostManagementIpInfoDto.class, sql, host);
    }

    @Override
    public void updateDockerHostStatus(String id, int status) {
        String sql = "dao.HostBaseInfoDao.updateDockerHostStatus";
        Map<String, Object> update = new HashMap<>();
        update.put("id", id);
        update.put("status", status);
        update(sql, update);
    }

    @Override
    public List<String> selectHostAllCpuFamily() {
        String sql = "dao.HostBaseInfoDao.selectHostAllCpuFamily";
        return selectList(String.class, sql, null);
    }

    @Override
    public List<Integer> selectHostAllCpuCoreLimit() throws DaoException {
        String sql = "dao.HostBaseInfoDao.selectHostAllCpuCoreLimit";
        return selectList(Integer.class, sql, null);
    }

    @Override
    public List<String> selectHostListByCondition(String cpuFamily, Integer cpuCoreLimit, Integer memoryLimit, Integer cpuPresentMode) throws DaoException {
        String sql = "dao.HostBaseInfoDao.selectHostListByCondition";
        Map<String, Object> condition = new HashMap<String, Object>();
        condition.put("cpu_model", cpuFamily);
        condition.put("cpu_cores", cpuCoreLimit);
        condition.put("memory_total", memoryLimit);
        condition.put("cpu_present_mode", cpuPresentMode);
        return selectList(String.class, sql, condition);
    }

    @Override
    public String selectHostIdByIp(String ip) throws DaoException {
        String sql = "dao.HostBaseInfoDao.selectHostIdByIp";
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ip", ip);
        return selectOne(String.class, sql, param);
    }

    @Override
    public List<HostMemoryInfoDto> selectHostMemoryInfo() throws DaoException {
        List<HostMemoryInfoDto> hostMemoryInfoDtos = new ArrayList<HostMemoryInfoDto>();
        int[] array = {1, 4, 8, 16};
        for (int i : array) {
            HostMemoryInfoDto hostMemoryInfoDto = new HostMemoryInfoDto();
            hostMemoryInfoDto.setMemoryLimit(i);
            hostMemoryInfoDto.setMemorySwapLimit(2 * i);
            hostMemoryInfoDtos.add(hostMemoryInfoDto);
        }
        LOGGER.debug(JsonUtil.toJson(hostMemoryInfoDtos));
        return hostMemoryInfoDtos;
    }

    @Override
    public List<HostBaseInfoDto> selectAllHostInfo() throws DaoException {
        String sql = "dao.HostBaseInfoDao.selectAllHostInfo";
        return selectList(HostBaseInfoDto.class, sql, null);
    }

    @Override
    public HostBaseInfoDto selectHostInfoById(String id) throws DaoException {
        String sql = "dao.HostBaseInfoDao.selectHostInfoById";
        return selectOne(HostBaseInfoDto.class, sql, id);
    }

    @Override
    public void insertHostNetworkCard(HostNetworkCardDto card) throws DaoException {
        String sql = "dao.HostBaseInfoDao.insertHostNetworkCard";
        insert(sql, card);
    }

    @Override
    public HostNetworkCardDto selectHostNetworkCardByHostAndMac(String id, String host) throws DaoException {
        String sql = "dao.HostBaseInfoDao.selectHostNetworkCardByHostAndMac";
        Map<String, Object> m = new HashMap<>();
        m.put("id", id);
        m.put("host", host);
        return selectOne(HostNetworkCardDto.class, sql, host);
    }

    @Override
    public void updateHostTail(HostCreateParam param) {
        String sql = "dao.HostBaseInfoDao.updateHostTail";
        Map<String, Object> update = new HashMap<>();
        update.put("ip", param.getHostip());
        update.put("status", 1);
        update.put("cluster",param.getSpecifyCluster());
        update.put("name",param.getHostname());
        update.put("lastModify",new Date());
        update(sql,update);
    }


    @Override
    public HostTailDto selectHostInfoByDescription(String description) throws DaoException {
        String sql = "dao.HostBaseInfoDao.selectHostInfoByDescription";
        return selectOne(HostTailDto.class,sql,description);
    }

    @Override
    public void updateHostByIp(HostStatusUpdateParam param) throws DaoException {
        String sql = "dao.HostBaseInfoDao.updateHostByIp";
        Map<String,Object> map=new HashMap<>();
        map.put("ip",param.getHostip());
        map.put("status",param.getStatus());
        map.put("lastModify",new Date());
        update(sql,map);
    }

    @Override
    public void deleteFromHostInfoBase(String id) throws DaoException {
        String sql="dao.HostBaseInfoDao.deleteFromHostInfoBase";
        delete(sql,id);
    }

    @Override
    public List<HostBaseInfoDto> selectHostByCluster(String id) throws DaoException {
        String sql="dao.HostBaseInfoDao.selectHostByCluster";
        return selectList(HostBaseInfoDto.class,sql,id);
    }

    @Override
    public String selectLabelByType() {
        String sql="dao.HostBaseInfoDao.selectLabelByType";
        return selectOne(String.class,sql,null);
    }

    @Override
    public HostExtInfoDto selectHExtDtoByHostId(String hostId) throws DaoException {
        String sql="dao.HostBaseInfoDao.selectHExtDtoByHostId";
        Map<String,Object>  param=new HashMap<>();
        param.put("host",hostId);
        return selectOne(HostExtInfoDto.class,sql,param);
    }

    @Override
    public void deleteFromHostInfoManagementIp(String id) throws DaoException {
        String sql="dao.HostBaseInfoDao.deleteFromHostInfoManagementIp";
        delete(sql,id);
    }

    @Override
    public void deleteFromHostInfoExt(String id) throws DaoException {
        String sql="dao.HostBaseInfoDao.deleteFromHostInfoExt";
        delete(sql,id);
    }

    @Override
    public void deleteFromDockerHostInfo(String id) throws DaoException {
        String sql="dao.HostBaseInfoDao.deleteFromDockerHostInfo";
        delete(sql,id);
    }

    @Override
    public void deleteFromHostNetworkCard(String id) throws DaoException {
        String sql="dao.HostBaseInfoDao.deleteFromHostNetworkCard";
        delete(sql,id);
    }

}
