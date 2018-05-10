package com.anyun.common.jbi.component;

import java.util.Date;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/14/16
 */
public class ComponentServiceRegistInfo {
    private Date upTime = new Date();
    private boolean available = true;
    private String component;

    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }
}
