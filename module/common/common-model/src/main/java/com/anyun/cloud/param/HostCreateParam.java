package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by zy on 17-3-28.
 */
public class HostCreateParam extends AbstractEntity {
    private String specifyCluster; //指定集群
    private String hostname; //宿主机名称
    private String describe; //描述
    private String hostip; //宿主机ip地址
    private int port; //端口
    private String username; //用户名
    private String password; //密码
    private String userUniqueId;//用户唯一标识

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getSpecifyCluster() {
        return specifyCluster;
    }

    public void setSpecifyCluster(String specifyCluster) {
        this.specifyCluster = specifyCluster;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getHostip() {
        return hostip;
    }

    public void setHostip(String hostip) {
        this.hostip = hostip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
