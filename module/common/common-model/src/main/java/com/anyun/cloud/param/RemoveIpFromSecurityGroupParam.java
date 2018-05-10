package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-10-19.
 */
public class RemoveIpFromSecurityGroupParam extends AbstractEntity {
    private String containerNetIpId;
    private String securityGroupId;
    private String userUniqueId;

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
