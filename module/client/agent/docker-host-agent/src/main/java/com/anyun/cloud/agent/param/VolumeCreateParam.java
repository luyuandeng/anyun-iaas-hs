package com.anyun.cloud.agent.param;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by twitchgg on 16-8-10.
 */
public class VolumeCreateParam extends AbstractEntity {
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
