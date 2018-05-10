package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * Created by admin on 2017/5/10.
 */
public class TagCreateParam extends AbstractEntity {
    private String __userTag__;
    private String resourceId;
    private String resourceType;

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
}
