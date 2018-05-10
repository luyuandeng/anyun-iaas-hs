package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

public class DiskSchemeCreateParam extends AbstractEntity {
    /**
     * 名称 (必填)
     */
    private String name;

    /**
     * 描述 (必填)
     */
    private String description;
    /**
     * 磁盘大小 (单位为 MB)
     * 最小是 20 MB
     */
    private long size;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
