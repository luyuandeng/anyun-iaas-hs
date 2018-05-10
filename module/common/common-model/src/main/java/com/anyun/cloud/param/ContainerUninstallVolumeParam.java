package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-8-11.
 */
public class ContainerUninstallVolumeParam extends AbstractEntity {
    private String container;  //容器
    private String volume;     //卷
    private String userUniqueId;//用户唯一标识

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
