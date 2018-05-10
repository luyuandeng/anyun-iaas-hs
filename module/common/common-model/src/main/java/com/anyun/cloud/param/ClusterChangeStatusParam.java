package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by jt-workspace on 17-4-25.
 */
public class ClusterChangeStatusParam extends AbstractEntity {
    private String id;                  //集群主键
    private String status;               //状态：Enable（可用） Disable（禁用）
    private String userUniqueId;        //用户唯一标识

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }
}
