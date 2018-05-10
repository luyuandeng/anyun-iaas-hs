package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by gaopeng on 16-7-13.
 */
public class NetworkCreateParam extends AbstractEntity {
    private  String name;
    private  String descript;
    private  String segment;
    private  String gateway;
    private  String subnetMast;
    private  String projectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getSubnetMast() {
        return subnetMast;
    }

    public void setSubnetMast(String subnetMast) {
        this.subnetMast = subnetMast;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
