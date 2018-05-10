package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * 计算服务方案
 */
public class CalculationSchemeDto extends AbstractEntity {
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
     * 内存 (单位为 MB)
     * 最小是 4 MB
     * 1 B 就是 1个字节。
     * 1 KB 叫 1 千字节。
     * 1 MB 叫 1 兆字节。
     * 1 GB 叫 1 吉字节。
     * 它们之间的换算关系是：
     * 1 KB =1024 B
     * 1 MB =1024 KB
     * 1 GB =1024 MB
     */
    private long memory;

    /**
     * CPU 利用率权重(2~262144)
     */
    private Integer cpuShares;

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

    public long getMemory() {
        return memory;
    }

    public void setMemory(long memory) {
        this.memory = memory;
    }

    public Integer getCpuShares() {
        return cpuShares;
    }

    public void setCpuShares(Integer cpuShares) {
        this.cpuShares = cpuShares;
    }
}
