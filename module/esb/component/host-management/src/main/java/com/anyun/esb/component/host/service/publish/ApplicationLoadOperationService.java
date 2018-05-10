package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.ContainerOpParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.ApplicationService;
import com.anyun.esb.component.host.service.docker.impl.ApplicationServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 17-6-2.
 */
public class ApplicationLoadOperationService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationLoadOperationService.class);
    private ApplicationService applicationService;

    public ApplicationLoadOperationService() {
        applicationService=new ApplicationServiceImpl();
    }

    @Override
    public String getName() {
        return "load_operation";
    }

    @Override
    public String getDescription() {
        return "ApplicationLoadOperationService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        ContainerOpParam param = JsonUtil.fromJson(ContainerOpParam.class, postBody);
        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(), "POST");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }


        try {
            applicationService.operationLoad(param);
            return new Status<String>("success");
        } catch (Exception ex) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("load operate  error  info[" + ex.getMessage() + "]");
            exception.setUserMessage("load operate  error [" + ex.getMessage() + "]");
            exception.setUserTitle("load operate  error");
            throw exception;
        }
    }
}
