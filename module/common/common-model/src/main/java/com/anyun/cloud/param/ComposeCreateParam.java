package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;


/**
 * Created by sxt on 16-12-7.
 */
public class ComposeCreateParam extends AbstractEntity {
    private String name; //项目名称
    private String descript;//项目描述
    private int space;//项目大小
    private String userUniqueId;//用户唯一标识
    private String version;//版本
    private String template;//模板

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

    public int getSpace() {
        return space;
    }

    public void setSpace(int space) {
        this.space = space;
    }

    public String getUserUniqueId() {
        return userUniqueId;
    }

    public void setUserUniqueId(String userUniqueId) {
        this.userUniqueId = userUniqueId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
