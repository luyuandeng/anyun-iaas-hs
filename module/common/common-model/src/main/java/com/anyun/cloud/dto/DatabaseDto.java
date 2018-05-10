package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * Created by sxt on 7/4/17.
 */
public class DatabaseDto extends AbstractEntity {
    private String id; //主键
    private String name;//名称
    private String describe;//描述
    private String projectId;//项目ID
    private String type; //  single：单点   cluster：集群
    private String bridgeL3Ip;  //  bridge 类型的l3网络标签 分配的 ip
    private String bridgeL3;  //  bridge 类型的l3网络标签
    private Date createTime; //创建时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBridgeL3Ip() {
        return bridgeL3Ip;
    }

    public void setBridgeL3Ip(String bridgeL3Ip) {
        this.bridgeL3Ip = bridgeL3Ip;
    }

    public String getBridgeL3() {
        return bridgeL3;
    }

    public void setBridgeL3(String bridgeL3) {
        this.bridgeL3 = bridgeL3;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
