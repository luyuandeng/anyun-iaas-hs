package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * Created by sxt on 16-7-13.
 */
public class ProjectDto extends AbstractEntity {
    private String id;         // 项目id
    private String name;       // 项目名称
    private String descript;   // 项目描述
    private Integer space;     // 项目容量（单位G）
    private Date createDate;   // 创建时间
    private Date lastModifyDate; //最后修改时间
    private String platFormNetworkId;// 项目网络标签
    private int allContanr;   //所有容器数量
    private int runContainer; //运行中的容器数量
    private String screateDate;
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

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Integer getSpace() {
        return space;
    }

    public void setSpace(Integer space) {
        this.space = space;
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

    public String getPlatFormNetworkId() {
        return platFormNetworkId;
    }

    public void setPlatFormNetworkId(String platFormNetworkId) {
        this.platFormNetworkId = platFormNetworkId;
    }

    public int getAllContanr() {
        return allContanr;
    }

    public void setAllContanr(int allContanr) {
        this.allContanr = allContanr;
    }

    public int getRunContainer() {
        return runContainer;
    }

    public void setRunContainer(int runContainer) {
        this.runContainer = runContainer;
    }

    public String getScreateDate() {
        return screateDate;
    }

    public void setScreateDate(String screateDate) {
        this.screateDate = screateDate;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }
}




