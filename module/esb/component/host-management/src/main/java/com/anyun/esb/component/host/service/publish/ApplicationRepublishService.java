package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.ApplicationService;
import com.anyun.esb.component.host.service.docker.impl.ApplicationServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by sxt on 17-6-5.
 */
public class ApplicationRepublishService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationRepublishService.class);
    private ApplicationService applicationService;

    public ApplicationRepublishService() {
        applicationService = new ApplicationServiceImpl();
    }

    @Override
    public String getName() {
        return "application_republish";
    }

    @Override
    public String getDescription() {
        return "ApplicationRepublishService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            LOGGER.debug("postBody is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            return applicationService.republish(postBody);
        } catch (Exception ex) {
            LOGGER.debug("Exception:[{}]", ex);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("application republish error [" + ex + "]");
            exception.setUserMessage("应用重新发布失败[" + ex + "]");
            exception.setUserTitle("应用重新发布失败");
            throw exception;
        }
    }
}
