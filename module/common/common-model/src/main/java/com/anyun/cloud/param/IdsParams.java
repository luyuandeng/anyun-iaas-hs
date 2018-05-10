package com.anyun.cloud.param;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 7/5/16
 */
public class IdsParams extends AbstractEntity {
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
