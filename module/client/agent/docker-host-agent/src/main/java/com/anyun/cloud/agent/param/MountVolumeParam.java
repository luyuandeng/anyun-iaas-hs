package com.anyun.cloud.agent.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sugs on 16-8-15.
 */
public class MountVolumeParam  extends AbstractEntity{
    private String containerId;
    private String volumeId;
    private String containerMountPath;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getContainerMountPath() {
        return containerMountPath;
    }

    public void setContainerMountPath(String containerMountPath) {
        this.containerMountPath = containerMountPath;
    }
}
