package com.anyun.esb.component.message;

import com.anyun.common.jbi.component.ComponentUtil;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author twitchgg@yahoo.com
 * @version 1.0
 * @Date 4/14/16
 */
public class ComponentProducer extends DefaultProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentProducer.class);
    private String option = "";
    public ComponentProducer(Endpoint endpoint) {
        super(endpoint);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        LOGGER.debug("Component producer option [{}]",option);
        String serialNumber = ComponentUtil.getMessageComponentSerialNumber();
        LOGGER.debug("Message component serial number [{}]",serialNumber);
        if(option.equals("get_serialnumber"))
            exchange.getOut().setBody(serialNumber);
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
