package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-10-25.
 */
public class ApplicationCreateParam extends AbstractEntity {
    private String userUniqueId;//用户唯一标识
    private String name;   //应用名称
    private String description;//应用描述
    private String type;// 应用类型   目前 只有一种：  WEB
    private String templateContainer; //模板容器
    private String label;// bridge  类型 网络标签
    private String ip; //  NG  bridge  类型ip
    private int nginxPort;//NG 端口   http服务端口为 80    htts服务 端口 443
    private int loadPort;//负载端口
    private int loadsTotal;// 负载总数  >=2
    private String weightType;// 权重类型   ip_hash (每个请求按访问ip的hash结果分配，这样每个访客固定访问一个后端服务器，可以解决session的问题) /least_conn(Web请求会被转发到连接数最少的服务器上)
    private String certificate; //服务器 证书
    private String privateKey; //服务器  私钥

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplateContainer() {
        return templateContainer;
    }

    public void setTemplateContainer(String templateContainer) {
        this.templateContainer = templateContainer;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getNginxPort() {
        return nginxPort;
    }

    public void setNginxPort(int nginxPort) {
        this.nginxPort = nginxPort;
    }

    public int getLoadPort() {
        return loadPort;
    }

    public void setLoadPort(int loadPort) {
        this.loadPort = loadPort;
    }

    public int getLoadsTotal() {
        return loadsTotal;
    }

    public void setLoadsTotal(int loadsTotal) {
        this.loadsTotal = loadsTotal;
    }

    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
