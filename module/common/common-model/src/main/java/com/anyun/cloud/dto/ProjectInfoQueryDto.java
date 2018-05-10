package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;

/**
 * Created by sxt on 16-8-17.
 */
public class ProjectInfoQueryDto extends AbstractEntity {
    private List<ContainerDto> containerDtoList;
    private List<NetLabelInfoDto> netLabelInfoDtoList;
    private List<NetLabelIpContainerDto> netLabelIpContainerDtoList; //网络标签和容器的绑定关系

    public List<ContainerDto> getContainerDtoList() {
        return containerDtoList;
    }

    public void setContainerDtoList(List<ContainerDto> containerDtoList) {
        this.containerDtoList = containerDtoList;
    }

    public List<NetLabelInfoDto> getNetLabelInfoDtoList() {
        return netLabelInfoDtoList;
    }

    public void setNetLabelInfoDtoList(List<NetLabelInfoDto> netLabelInfoDtoList) {
        this.netLabelInfoDtoList = netLabelInfoDtoList;
    }

    public List<NetLabelIpContainerDto> getNetLabelIpContainerDtoList() {
        return netLabelIpContainerDtoList;
    }

    public void setNetLabelIpContainerDtoList(List<NetLabelIpContainerDto> netLabelIpContainerDtoList) {
        this.netLabelIpContainerDtoList = netLabelIpContainerDtoList;
    }
}
