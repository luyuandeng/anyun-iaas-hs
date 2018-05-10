package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;


/**
 * Created by sxt on 7/4/17.
 */
public class DatabaseCreateParam extends AbstractEntity {
    private String userUniqueId;//用户唯一标识
    private String projectId;//项目ID
    private String name;//名称
    private String describe;//描述
    private String type; // single：单点   cluster：集群
    private int sqldNodeTotal = 2; // mysqld节点(和NDBD节点共用容器) 数量  如果type 为 cluster ，则 数量大于等于2 ,其他不做判断
    private String bridgeL3Ip;  //  bridge 类型的l3网络标签 分配的 ip
    private String bridgeL3;  //  bridge 类型的l3网络标签
    private String userName;// 数据库用户名(root，111111 为默认 用户名和密码)
    private String password;// 数据库密码

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSqldNodeTotal() {
        return sqldNodeTotal;
    }

    public void setSqldNodeTotal(int sqldNodeTotal) {
        this.sqldNodeTotal = sqldNodeTotal;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
