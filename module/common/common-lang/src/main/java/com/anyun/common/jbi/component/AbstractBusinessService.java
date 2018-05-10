package com.anyun.common.jbi.component;

import com.anyun.cloud.tools.StringUtils;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/14/16
 */
public abstract class AbstractBusinessService implements BusinessService {
    private String serviceId;
    private static final ThreadLocal<ApplicationContext> APPLICATION_CONTEXT = new ThreadLocal<>();

    public AbstractBusinessService() {
        this.serviceId = StringUtils.uuidGen();
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    public ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT.get();
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        APPLICATION_CONTEXT.set(context);
    }
}
