package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;


/**
 * Created by hwt on 16-7-13.
 */
public class VolumeCreateParam extends AbstractEntity {
    private String name; //名称
    private String descr;//描述
    private int space;//卷大小（G）
    private String project;//所属项目
    private String userUniqueId;
    private String storageId;

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
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

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }
}
