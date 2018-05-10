package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/4/16
 */
public class DockerImageDto extends AbstractEntity {

    /**
     * 镜像ID
     */
    private String id;

    /**
     * 镜像分类名称
     */
    private String category;


    /**
     * 镜像名称
     */
    private String name;

    /**
     * 镜像标签
     */
    private String tag;

    /**
     * 镜像描述
     */
    private String descript;

    /**
     * 图片名称
     */
    private String icon;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 最新修改时间
     */
    private Date lastModifyDate;

    @Override
    public String toString() {
        return "DockerImageDto{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", tag='" + tag + '\'' +
                ", descript='" + descript + '\'' +
                ", icon='" + icon + '\'' +
                ", createDate=" + createDate +
                ", lastModifyDate=" + lastModifyDate +
                ", status=" + status +
                ", userDefineName='" + userDefineName + '\'' +
                ", screateDate='" + screateDate + '\'' +
                ", slastModifyDate='" + slastModifyDate + '\'' +
                '}';
    }

    /**
     * 有效区分
     */


    private int status;

    private String userDefineName;

    private String screateDate;

    public String getScreateDate() {
        return screateDate;
    }

    public void setScreateDate(String screateDate) {
        this.screateDate = screateDate;
    }

    public String getSlastModifyDate() {
        return slastModifyDate;
    }

    public void setSlastModifyDate(String slastModifyDate) {
        this.slastModifyDate = slastModifyDate;
    }

    private String slastModifyDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserDefineName() {
        return userDefineName;
    }

    public void setUserDefineName(String userDefineName) {
        this.userDefineName = userDefineName;
    }
}
