package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.ComposeCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.ComposeService;
import com.anyun.esb.component.host.service.docker.impl.ComposeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-12-6.
 */
public class ComposeCreateService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ComposeCreateService.class);
    private ComposeService composeService;

    public ComposeCreateService() {
        composeService = new ComposeServiceImpl();
    }

    @Override
    public String getName() {
        return "compose_create";
    }

    @Override
    public String getDescription() {
        return "ComposeCreateService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String body = exchange.getIn().getBody(String.class);
        LOGGER.debug("body:[{}]", body);
        if (StringUtils.isEmpty(body)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("body is empty");
            exception.setUserMessage("body is empty");
            exception.setUserTitle("body is empty");
            throw exception;
        }
        ComposeCreateParam param;
        try {
            param = JsonUtil.fromJson(ComposeCreateParam.class, body);
            LOGGER.debug("param:[{}]", param.asJson());
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("格式 转换 异常：[{" + e.getMessage() + "}]");
            exception.setUserMessage("格式 转换 异常");
            exception.setUserTitle("格式 转换 异常");
            throw exception;
        }

        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("param is empty");
            throw exception;
        }
        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"PUT");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }


        if (param.getSpace() <= 0) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("space <= 0");
            exception.setUserMessage("space <= 0");
            exception.setUserTitle("space <= 0");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getVersion())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("version  is empty");
            exception.setUserMessage("version is empty");
            exception.setUserTitle("version is empty");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getTemplate())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("template is empty");
            exception.setUserMessage("template is empty");
            exception.setUserTitle("template is empty");
            throw exception;
        }

        try {
            composeService.createCompose(param);
            return new Status<String>();
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Compose Create fail：[{" + e.getMessage() + "}]");
            exception.setUserMessage("Compose Create fail");
            exception.setUserTitle("Compose Create fail");
            throw exception;
        }
    }
}
