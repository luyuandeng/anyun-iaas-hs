package com.anyun.cloud.agent.result;

/**
 * Created by twitchgg on 16-8-3.
 */
public class StorageInfo {
    private String type;
    private String filesystem;
    private float size;
    private float used;
    private float avail;
    private float usedPercentage;
    private String mountedOn;

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