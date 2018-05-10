package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by admin on 2016/10/17.
 */
public class PlatformUpdateParam extends AbstractEntity {
    private String id;         // id
    private String name;  //名称
    private String description; //描述
    private String ipDomain; //ip 或 域名
    private String port;    //端口
    private String baseUrl; // 基本路径
    private String area;    //所属区域
    private int status;  // 0 或 1
    private String userUniqueId;//用户唯一标识

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpDomain() {
        return ipDomain;
    }

    public void setIpDomain(String ipDomain) {
        this.ipDomain = ipDomain;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }
}
