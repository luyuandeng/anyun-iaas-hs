package com.anyun.common.jbi.component;

import java.util.Date;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/14/16
 */
public class ComponentRegistInfo {
    private Date upTime = new Date();
    private boolean available = true;
    private String messageNode;

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

    public String getMessageNode() {
        return messageNode;
    }

    public void setMessageNode(String messageNode) {
        this.messageNode = messageNode;
    }
}
