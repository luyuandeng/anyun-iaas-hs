package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by gp on 16-9-22.
 */
public class MemoryDto extends AbstractEntity {
    private String usage;
    private String memoryCapacity;

    @Override
    public String toString() {
        return "MemoryDto{" +
                "usage='" + usage + '\'' +
                ", memoryCapacity='" + memoryCapacity + '\'' +
                '}';
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(String memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }
}
