package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-7-25.
 */
public class DisconnectFromNetParam extends AbstractEntity {
    private String container;//容器Id
    private String label;//网络标签
    private String userUniqueId;//用户唯一标识

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }
}
