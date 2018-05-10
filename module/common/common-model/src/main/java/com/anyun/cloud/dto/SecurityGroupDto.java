package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;


/**
 * 安全组
 * Created by sxt on 16-10-17.
 */

public class SecurityGroupDto extends AbstractEntity {
    private String label; //标签
    private String name; //名称
    private String description;//描述
    private String port; // 端口号(一个端口号 如：8080)
    private String ipOrSegment; //ip 或者 ip段
    private String rule;  //规则   ALLOW_ACCESS:允许访问    ALLOW_IN：只允许进    ALLOW_OUT：只允许出
    private Date createDate;//创建时间;
    private String project; //项目id
    private String screateDate;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getScreateDate() {
        return screateDate;
    }

    public void setScreateDate(String screateDate) {
        this.screateDate = screateDate;
    }
}
