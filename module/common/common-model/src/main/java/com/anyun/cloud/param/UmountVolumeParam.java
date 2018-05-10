package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sugs on 16-8-18.
 */
public class UmountVolumeParam extends AbstractEntity {
    private String volumeId;
    private String containerId;
    private String containerMountPath;

    public String getVolumeId() {
        return volumeId;
    }

    public UmountVolumeParam setVolumeId(String volumeId) {
        this.volumeId = volumeId;
        return this;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getContainerMountPath() {
        return containerMountPath;
    }

    public void setContainerMountPath(String containerMountPath) {
        this.containerMountPath = containerMountPath;
    }
}
