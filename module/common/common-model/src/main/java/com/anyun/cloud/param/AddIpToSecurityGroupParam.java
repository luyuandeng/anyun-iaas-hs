package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-10-18.
 */
public class AddIpToSecurityGroupParam extends AbstractEntity {
    private String containerNetIpId;//容器网络表  Id
    private String securityGroupId;//安全组表  Id
    private String userUniqueId; //用户唯一标识

    public String getContainerNetIpId() {
        return containerNetIpId;
    }

    public void setContainerNetIpId(String containerNetIpId) {
        this.containerNetIpId = containerNetIpId;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }
}
