package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class DockerImageCategoryDto extends AbstractEntity{
    private String id;//镜像ID
    private String name;//镜像分类名称
    private String shortName;//镜像分类短名称
    private String descript;//镜像描述
    private Date dateCreate;//创建时间
    private Date dateLastModify;//最新修改时间
    private String sdateCreate;//创建时间 String類型
    private int status;// 用戶狀態（默认1为正常用,0为禁用）


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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Date getDateCreate() {
        return dateCreate;
    }

    public void setDateCreate(Date dateCreate) {
        this.dateCreate = dateCreate;
    }

    public Date getDateLastModify() {
        return dateLastModify;
    }

    public void setDateLastModify(Date dateLastModify) {
        this.dateLastModify = dateLastModify;
    }

    public String getSdateCreate() {
        return sdateCreate;
    }

    public void setSdateCreate(String sdateCreate) {
        this.sdateCreate = sdateCreate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
