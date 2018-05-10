package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by gp on 17-3-28.
 */
public class ClusterCreateParam extends AbstractEntity {
    private String name;                //区域名称
    private String description;         //描述
    private String status = "Enable";   //状态：Enable（可用） Disable（禁用）
    private String userUniqueId;        //用户唯一标识

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
