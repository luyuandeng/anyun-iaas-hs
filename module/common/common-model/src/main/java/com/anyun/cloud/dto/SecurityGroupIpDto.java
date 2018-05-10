package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-10-19.
 */
public class SecurityGroupIpDto  extends AbstractEntity{
    private String id;
    private String containerNetIpId;
    private String securityGroupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

}
