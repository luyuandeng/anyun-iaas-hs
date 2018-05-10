package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-10-13.
 */
public class PictureDto extends AbstractEntity {
    private  String     id;
    private  String    maxPicUrl;
    private  String    minPicUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMaxPicUrl() {
        return maxPicUrl;
    }

    public void setMaxPicUrl(String maxPicUrl) {
        this.maxPicUrl = maxPicUrl;
    }

    public String getMinPicUrl() {
        return minPicUrl;
    }

    public void setMinPicUrl(String minPicUrl) {
        this.minPicUrl = minPicUrl;
    }
}
