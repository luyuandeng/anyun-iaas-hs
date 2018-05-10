package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * 磁盘服务方案
 */
public class DiskSchemeDto extends AbstractEntity{
    /**
     * 主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;
    /**
     * 磁盘大小 (单位为 MB)
     * 最小是 20 MB
     * 1 B 就是 1个字节。
     * 1 KB 叫 1 千字节。
     * 1 MB 叫 1 兆字节。
     * 1 GB 叫 1 吉字节。
     * 它们之间的换算关系是：
     * 1 KB =1024 B
     * 1 MB =1024 KB
     * 1 GB =1024 MB
     */
    private long size;

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
