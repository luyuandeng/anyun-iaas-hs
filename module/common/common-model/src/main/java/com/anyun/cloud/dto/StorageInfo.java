package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by twitchgg on 16-8-3.
 */
public class StorageInfo extends AbstractEntity{
    private String type;
    private String filesystem; //文件系统(远端挂载点)
    private float size;  //容量（G）
    private float used;  //已用（G）
    private float avail; //可用 （G）
    private float usedPercentage;  //已用百分比
    private String mountedOn;  //挂载点(本地挂载点)

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public float getUsed() {
        return used;
    }

    public void setUsed(float used) {
        this.used = used;
    }

    public float getAvail() {
        return avail;
    }

    public void setAvail(float avail) {
        this.avail = avail;
    }

    public float getUsedPercentage() {
        return usedPercentage;
    }

    public void setUsedPercentage(float usedPercentage) {
        this.usedPercentage = usedPercentage;
    }

    public String getMountedOn() {
        return mountedOn;
    }

    public void setMountedOn(String mountedOn) {
        this.mountedOn = mountedOn;
    }

    public String getFilesystem() {
        return filesystem;
    }

    public void setFilesystem(String filesystem) {
        this.filesystem = filesystem;
    }
}