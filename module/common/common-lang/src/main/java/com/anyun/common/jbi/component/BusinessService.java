package com.anyun.common.jbi.component;

import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/13/16
 */
public interface BusinessService {
    String getName();

    String getDescription();

    Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException;

    String getServiceId();

    void setApplicationContext(ApplicationContext context);

    ApplicationContext getApplicationContext();
}
