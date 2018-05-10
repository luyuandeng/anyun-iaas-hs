package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-10-17.
 */
public class SecurityGroupCreateParam extends AbstractEntity {
    private String name; //名称
    private String description;//描述
    private String port; // 端口号(一个端口号 如：8080)
    private String ipOrSegment; //ip 或者 ip段
    private String rule;  //规则   ALLOW_ACCESS:允许访问
    private String project; //项目id
    private String userUniqueId;//用户唯一标识

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

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIpOrSegment() {
        return ipOrSegment;
    }

    public void setIpOrSegment(String ipOrSegment) {
        this.ipOrSegment = ipOrSegment;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }
}
