package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-8-11.
 */
public class StorageUpdateStateParam extends AbstractEntity {
    private String id;
    private String userUniqueId;//用户唯一标识
    private String availableState;  //可用状态

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAvailableState() {
        return availableState;
    }

    public void setAvailableState(String availableState) {
        this.availableState = availableState;
    }
}
