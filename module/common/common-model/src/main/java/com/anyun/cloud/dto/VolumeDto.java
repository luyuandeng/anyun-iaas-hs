package com.anyun.cloud.dto;

import java.util.Date;

/**
 * 卷信息
 * Created by sxt on 16-8-10.
 */
public class VolumeDto {
    private String id;
    private String name; //名称
    private String descr;//描述
    private int space;//卷大小（G）
    private String project;//所属项目
    private Date createDate;//创建时间
    private Date lastModifyDate;//最后修改时间
    private String screateDate;
    private String slastModifyDate;
    private String storageId;

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

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
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

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }
}
