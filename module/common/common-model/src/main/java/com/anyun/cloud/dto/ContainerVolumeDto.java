package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 *  容器挂载卷
 * Created by sxt on 16-8-10.
 */
public class ContainerVolumeDto extends AbstractEntity{
    private  String   id ;
    private  String   container;//容器
    private  String   volume;//卷
    private  String   mountPath;//挂载路径

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }
}
