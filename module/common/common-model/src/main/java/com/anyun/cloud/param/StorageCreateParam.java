package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-8-10.
 */
public class StorageCreateParam extends AbstractEntity {
    private String name;   //名称
    private String descr;  //描述
    private String filesystem;//文件系统路径(远端挂载点)
    private String purpose; //存储用途 (docker.runtime , docker.volume)
    private String type;   //存储类型 (gluster,yeestore,nfs)
    private String userUniqueId;//用户唯一标识
    private String linkState = "alreadyLink";   //连接状态
    private String availableState = "usable"; //可用状态


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getFilesystem() {
        return filesystem;
    }

    public void setFilesystem(String filesystem) {
        this.filesystem = filesystem;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLinkState() {
        return linkState;
    }

    public void setLinkState(String linkState) {
        this.linkState = linkState;
    }

    public String getAvailableState() {
        return availableState;
    }

    public void setAvailableState(String availableState) {
        this.availableState = availableState;
    }
}
