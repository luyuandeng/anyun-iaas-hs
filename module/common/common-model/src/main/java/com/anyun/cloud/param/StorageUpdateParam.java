package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by gp on 17-5-12.
 */
public class StorageUpdateParam  extends AbstractEntity{
    private String id;
    private String name;  //名称
    private String descr; //描述
    private String userUniqueId;//用户唯一标识
    private String availableState;  //可用状态

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

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getAvailableState() {
        return availableState;
    }

    public void setAvailableState(String availableState) {
        this.availableState = availableState;
    }
}
