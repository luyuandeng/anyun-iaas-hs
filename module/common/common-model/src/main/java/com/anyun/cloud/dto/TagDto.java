package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * Created by sxt on 17-5-4.
 */
public class TagDto extends AbstractEntity {
    private String id; //标签主键
    private String __userTag__; //标签
    private String resourceId;//资源主键
    private String resourceType;//资源类型
    private Date createDate;//创建时间
    private Date lastOpDate;//最后修改时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String get__userTag__() {
        return __userTag__;
    }

    public void set__userTag__(String __userTag__) {
        this.__userTag__ = __userTag__;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastOpDate() {
        return lastOpDate;
    }

    public void setLastOpDate(Date lastOpDate) {
        this.lastOpDate = lastOpDate;
    }
}
