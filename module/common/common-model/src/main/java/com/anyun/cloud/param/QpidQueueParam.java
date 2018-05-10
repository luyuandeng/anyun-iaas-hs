package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;

/**
 * Created by zy on 17-7-31.
 */
public class QpidQueueParam extends AbstractEntity {
    private String ip;
    private int port=8080;
    private String address="/api/latest/querybroker/queue";
    private String username;
    private String password;
    private String params;

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
