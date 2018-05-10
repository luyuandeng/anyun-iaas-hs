package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

public class ContainerChangeDiskSchemeParam extends AbstractEntity {
    /**
     * 容器id
     */
    private String id;
    /**
     * 磁盘方案id
     */
    private String diskSchemeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiskSchemeId() {
        return diskSchemeId;
    }

    public void setDiskSchemeId(String diskSchemeId) {
        this.diskSchemeId = diskSchemeId;
    }
}
