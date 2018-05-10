package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by gp on 16-12-14.
 */
public class ServiceOperationDto extends AbstractEntity {
    private String serviceName;//服务名称
    private String status;//服务状态

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

}
