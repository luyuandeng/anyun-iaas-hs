package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.PlatformSetDefaultParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.PlatformService;
import com.anyun.esb.component.host.service.docker.impl.PlatformServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-10-20.
 */
public class PlatformSetAsDefaultService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(PlatformSetAsDefaultService.class);
    private PlatformService platformService;

    public PlatformSetAsDefaultService() {
        platformService = new PlatformServiceImpl();
    }

    @Override
    public String getName() {
        return "platform_set_as_default";
    }

    @Override
    public String getDescription() {
        return "PlatformSetAsDefaultService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]",postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        PlatformSetDefaultParam   param= JsonUtil.fromJson(PlatformSetDefaultParam.class,postBody);
        LOGGER.debug("param:[{}]",param.asJson());
        if (param==null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("id is empty");
            exception.setUserMessage("is is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            platformService.platformSetAsDefault(param.getId());
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("PlatformSetAsDefault fail:" + e.getMessage());
            exception.setUserMessage("PlatformSetAsDefault fail");
            exception.setUserTitle("PlatformSetAsDefault fail");
            throw exception;
        }
    }
}
