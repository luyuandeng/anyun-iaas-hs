package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;

/**
 * Created by sxt on 16-12-8.
 */
public class TemplateComposeDto extends AbstractEntity {
    private List<ContainerDto> containerDtos;
    private List<NetLabelInfoDto> netLabelInfoDtos;
    private List<VolumeDto> volumeDtos;

    public List<ContainerDto> getContainerDtos() {
        return containerDtos;
    }

    public void setContainerDtos(List<ContainerDto> containerDtos) {
        this.containerDtos = containerDtos;
    }

    public List<NetLabelInfoDto> getNetLabelInfoDtos() {
        return netLabelInfoDtos;
    }

    public void setNetLabelInfoDtos(List<NetLabelInfoDto> netLabelInfoDtos) {
        this.netLabelInfoDtos = netLabelInfoDtos;
    }

    public List<VolumeDto> getVolumeDtos() {
        return volumeDtos;
    }

    public void setVolumeDtos(List<VolumeDto> volumeDtos) {
        this.volumeDtos = volumeDtos;
    }
}
