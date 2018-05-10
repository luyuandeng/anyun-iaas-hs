package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by admin on 2017/5/10.
 */
public class TagUpdateParam extends AbstractEntity {
    private String id;
    private String __userTag__;
    private String resourceId;
    private String resourceType;

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
}
