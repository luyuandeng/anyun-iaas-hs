package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by admin on 2017/4/11.
 */
public class NetL2CreateParam extends AbstractEntity {
    private String name;
    private String description;
    private String cluster;
    private String physical_interface;
    private String type;

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

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getPhysical_interface() {
        return physical_interface;
    }

    public void setPhysical_interface(String physical_interface) {
        this.physical_interface = physical_interface;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
