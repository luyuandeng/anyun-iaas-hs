package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.PlatformCreateParam;
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
 * Created by sxt on 16-10-13.
 */
public class PlatformCreateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformCreateService.class);
    private PlatformService platformService;

    public PlatformCreateService() {
        platformService = new PlatformServiceImpl();
    }

    @Override
    public String getName() {
        return "platform_create";
    }

    @Override
    public String getDescription() {
        return "PlatformCreateService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        PlatformCreateParam param = JsonUtil.fromJson(PlatformCreateParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("参数验证失败");
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

        if (StringUtils.isEmpty(param.getIpDomain())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("ipDomain is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        //IP 合法校驗  域名合法校驗
        String regexip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        String regexdomain = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";//域名的正則表達式


        if(!param.getIpDomain().matches(regexip) && !param.getIpDomain().matches(regexdomain)){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("ipDomain is error");
            exception.setUserMessage("param is error");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getPort())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("port is empty");
            exception.setUserMessage("port is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        if (StringUtils.isEmpty(param.getBaseUrl())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("baseUrl is empty");
            exception.setUserMessage("baseUrl is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (param.getStatus() != 0 && param.getStatus() != 1) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("The param status is  not  in (0,1)");
            exception.setUserMessage("The param status is  not  in (0,1)");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            platformService.platformCreate(param);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("PlatformCreate  fail [" + e.getMessage() + "]");
            exception.setUserMessage("PlatformCreate  fail： [" + e.getMessage() + "]");
            exception.setUserTitle("PlatformCreate fail");
            throw exception;
        }
    }
}
