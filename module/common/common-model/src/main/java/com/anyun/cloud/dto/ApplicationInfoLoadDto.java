package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * 负载
 * Created by sxt on 16-10-25.
 */
public class ApplicationInfoLoadDto extends AbstractEntity {
    private String application;  //应用
    private String loadContainer; //负载 主键

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getLoadContainer() {
        return loadContainer;
    }

    public void setLoadContainer(String loadContainer) {
        this.loadContainer = loadContainer;
    }
}
