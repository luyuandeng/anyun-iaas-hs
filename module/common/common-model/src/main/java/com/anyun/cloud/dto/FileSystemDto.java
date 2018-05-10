package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by gp on 16-9-21.
 */
public class FileSystemDto extends AbstractEntity{
    private String usage;  //文件名称
    private String device; //文件路径
    private String capacity;//文件容量


    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "FileSystemDto{" +
                "usage='" + usage + '\'' +
                ", device='" + device + '\'' +
                ", capacity='" + capacity + '\'' +
                '}';
    }
}
