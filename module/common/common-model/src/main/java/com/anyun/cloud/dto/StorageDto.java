package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * 存储信息
 * Created by sxt on 16-8-10.
 */
public class StorageDto extends AbstractEntity {
    private String id;
    private String name;  //名称
    private String descr; //描述
    private String filesystem;//文件系统路径(远端挂载点)
    private String purpose; //存储用途 (docker.runtime , docker.volume)
    private String type;   //存储类型 (gluster,yeestore,nfs)
    private Date createDate; //创建时间
    private Date lastModifyDate;//最后修改时间
    private StorageInfo storageInfo;
    private String screateDate;
    private String slastModifyDate;
    private String linkState;   //连接状态
    private String availableState; //可用状态


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public StorageInfo getStorageInfo() {
        return storageInfo;
    }

    public void setStorageInfo(StorageInfo storageInfo) {
        this.storageInfo = storageInfo;
    }

    public String getScreateDate() {
        return screateDate;
    }

    public void setScreateDate(String screateDate) {
        this.screateDate = screateDate;
    }

    public String getSlastModifyDate() {
        return slastModifyDate;
    }

    public void setSlastModifyDate(String slastModifyDate) {
        this.slastModifyDate = slastModifyDate;
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
