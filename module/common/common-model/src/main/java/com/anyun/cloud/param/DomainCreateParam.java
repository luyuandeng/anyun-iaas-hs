package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;


/**
 * Created by gp on 17-2-22.
 */
public class DomainCreateParam extends AbstractEntity {
    private String id;      //域名管理ID
    private String domain;  //域名
    private String ip;      //容器IP
    private String containerId;//容器ID


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }
}
