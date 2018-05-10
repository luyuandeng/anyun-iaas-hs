package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;
import java.util.List;

/**
 * 应用
 * Created by sxt on 16-10-24.
 */
public class ApplicationInfoDto extends AbstractEntity {
    private String id; //应用表主键
    private String name;   //应用名称
    private String description;//应用描述
    private String type;// 应用类型
    private String accessPath;//访问路径
    private String weightType;// 权重类型    ip_hash/least_conn
    private Date createDate;//创建时间
    private String templateContainer; //模板容器
    private String nginxContainer;//nginx 容器
    private String label;//  bridge  类型  网络标签
    private String ip; //  NG  bridge  类型ip
    private int nginxPort;// nginx 端口
    private int loadPort;// load 端口
    private int loadsTotal;// 负载总数

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessPath() {
        return accessPath;
    }

    public void setAccessPath(String accessPath) {
        this.accessPath = accessPath;
    }

    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getTemplateContainer() {
        return templateContainer;
    }

    public void setTemplateContainer(String templateContainer) {
        this.templateContainer = templateContainer;
    }

    public String getNginxContainer() {
        return nginxContainer;
    }

    public void setNginxContainer(String nginxContainer) {
        this.nginxContainer = nginxContainer;
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
}



