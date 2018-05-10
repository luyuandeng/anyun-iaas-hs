package com.anyun.esb.component.message.core;

import com.anyun.common.jbi.component.ComponentRegistInfo;
import com.anyun.common.jbi.component.ComponentServiceRegistInfo;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/15/16
 */
public class ComponentServiceDefine {
    private String messageComponentId;
    private ComponentRegistInfo componentRegistInfo;
    private ComponentServiceRegistInfo componentServiceRegistInfo;

    public ComponentRegistInfo getComponentRegistInfo() {
        return componentRegistInfo;
    }

    public void setComponentRegistInfo(ComponentRegistInfo componentRegistInfo) {
        this.componentRegistInfo = componentRegistInfo;
    }

    public ComponentServiceRegistInfo getComponentServiceRegistInfo() {
        return componentServiceRegistInfo;
    }

    public void setComponentServiceRegistInfo(ComponentServiceRegistInfo componentServiceRegistInfo) {
        this.componentServiceRegistInfo = componentServiceRegistInfo;
    }

    public String getMessageComponentId() {
        return messageComponentId;
    }

    public void setMessageComponentId(String messageComponentId) {
        this.messageComponentId = messageComponentId;
    }
}
