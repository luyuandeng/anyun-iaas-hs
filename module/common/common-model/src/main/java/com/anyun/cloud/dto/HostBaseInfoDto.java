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

package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;
import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/23/16
 */
public class HostBaseInfoDto extends AbstractEntity {
    private String id;  //宿主机序列号
    private String name;//宿主机名称
    private String cluster;// 主机所在的集群
    private String descript;//描述
    private Date createDate;  // 创建时间
    private Date lastModify;//最近修改时间
    private int status = 1; //状态
    private HostApiInfoDto hostApiInfoDto;
    private HostApprovalDto hostApprovalDto;
    private HostManagementIpInfoDto hostManagementIpInfoDto; //宿主机管理ip信息
    private DockerHostInfoDto dockerHostInfoDto;//宿主机 docker 版本信息
    private HostExtInfoDto hostExtInfoDto; //宿主机 硬件信息
    private List<HostNetworkCardDto> hostNetworkCardDtoList; //宿主机网卡信息
    private ContainerOnHostDto containerOnHostDto;  // 在宿主机上的容器

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HostApiInfoDto getHostApiInfoDto() {
        return hostApiInfoDto;
    }

    public void setHostApiInfoDto(HostApiInfoDto hostApiInfoDto) {
        this.hostApiInfoDto = hostApiInfoDto;
    }

    public HostApprovalDto getHostApprovalDto() {
        return hostApprovalDto;
    }

    public void setHostApprovalDto(HostApprovalDto hostApprovalDto) {
        this.hostApprovalDto = hostApprovalDto;
    }

    public HostManagementIpInfoDto getHostManagementIpInfoDto() {
        return hostManagementIpInfoDto;
    }

    public void setHostManagementIpInfoDto(HostManagementIpInfoDto hostManagementIpInfoDto) {
        this.hostManagementIpInfoDto = hostManagementIpInfoDto;
    }

    public DockerHostInfoDto getDockerHostInfoDto() {
        return dockerHostInfoDto;
    }

    public void setDockerHostInfoDto(DockerHostInfoDto dockerHostInfoDto) {
        this.dockerHostInfoDto = dockerHostInfoDto;
    }

    public HostExtInfoDto getHostExtInfoDto() {
        return hostExtInfoDto;
    }

    public void setHostExtInfoDto(HostExtInfoDto hostExtInfoDto) {
        this.hostExtInfoDto = hostExtInfoDto;
    }

    public List<HostNetworkCardDto> getHostNetworkCardDtoList() {
        return hostNetworkCardDtoList;
    }

    public void setHostNetworkCardDtoList(List<HostNetworkCardDto> hostNetworkCardDtoList) {
        this.hostNetworkCardDtoList = hostNetworkCardDtoList;
    }

    public ContainerOnHostDto getContainerOnHostDto() {
        return containerOnHostDto;
    }

    public void setContainerOnHostDto(ContainerOnHostDto containerOnHostDto) {
        this.containerOnHostDto = containerOnHostDto;
    }
}
