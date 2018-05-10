package com.anyun.cloud.agent.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by twitchgg on 16-8-3.
 */
public class GlusterStorageAdd  extends AbstractEntity{
    private String gluserSrc;
    private String useType;

    public String getGluserSrc() {
        return gluserSrc;
    }

    public void setGluserSrc(String gluserSrc) {
        this.gluserSrc = gluserSrc;
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType;
    }
}
