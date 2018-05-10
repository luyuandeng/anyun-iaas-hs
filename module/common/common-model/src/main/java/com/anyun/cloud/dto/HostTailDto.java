package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * Created by zy on 17-4-17.
 */
public class HostTailDto extends AbstractEntity{
    private String id;//宿主机id
    private String cluster;//集群id
    private String name;// 宿主机id名稱
    private String description;// 宿主机描述
    private Date createDate;//创建时间
    private Date lastModify;//最後修改時間
    private int status;// 用戶狀態（默认1为正常用,0为禁用）

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModify() {
        return lastModify;
    }

    public void setLastModify(Date lastModify) {
        this.lastModify = lastModify;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
