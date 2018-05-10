package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sugs on 16-8-18.
 */
public class UpdateVolumeParam extends AbstractEntity {

    private int size;

    private String volumeId;

    private String containerMountPath;

    public String getContainerMountPath() {
        return containerMountPath;
    }

    public void setContainerMountPath(String containerMountPath) {
        this.containerMountPath = containerMountPath;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }
}
