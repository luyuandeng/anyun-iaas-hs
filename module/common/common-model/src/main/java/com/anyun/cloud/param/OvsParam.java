package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;

/**
 * Created by gp on 16-11-2.
 */
public class OvsParam extends AbstractEntity {
    private List<OvsBridgeParam> params;

    public List<OvsBridgeParam> getParams() {
        return params;
    }

    public void setParams(List<OvsBridgeParam> params) {
        this.params = params;
    }
}
