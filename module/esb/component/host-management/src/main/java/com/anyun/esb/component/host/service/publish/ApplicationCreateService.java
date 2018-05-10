package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.param.ApplicationCreateParam;
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
 * Created by sxt on 16-10-25.
 */
public class ApplicationCreateService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationCreateService.class);
    private ApplicationService applicationService;

    public ApplicationCreateService() {
        applicationService = new ApplicationServiceImpl();
    }

    @Override
    public String getName() {
        return "application_create";
    }

    @Override
    public String getDescription() {
        return "ApplicationCreateService";
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

        ApplicationCreateParam param = JsonUtil.fromJson(ApplicationCreateParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(), "PUT");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }


        if (StringUtils.isEmpty(param.getType())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("type is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (!"WEB".equals(param.getType())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("The type is incorrect [" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getIp())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("ip  is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        //校驗ip
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\." + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";//IP正则表达式
        if (!param.getIp().matches(regex)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[ip] is Error IP");
            exception.setUserMessage("service param[ip] Error IP");
            exception.setUserTitle("参数 ip 是错误IP");
            throw exception;
        }


        if (param.getNginxPort() == 443) {
            if (StringUtils.isEmpty(param.getCertificate())) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setUserMessage("Service param format error");
                exception.setMessage("certificate  is  empty[" + exception + "]");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }
            if (StringUtils.isEmpty(param.getPrivateKey())) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setUserMessage("Service param format error");
                exception.setMessage("privateKey  is  empty[" + exception + "]");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }
        } else {
            if (param.getNginxPort() != 80) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setUserMessage("Service param format error");
                exception.setMessage("port  is  error[" + exception + "]");
                exception.setUserTitle("参数验证失败");
                throw exception;
            }
        }


        if (param.getLoadsTotal() < 2) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage(" param.getLoadsTotal()<2 [" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getTemplateContainer())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage(" container is  empty [" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getLabel())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("label is  empty [" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getWeightType())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("weightType  is  empty [" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (!"least_conn".equals(param.getWeightType()) && !"ip_hash".equals(param.getWeightType())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("weightType is  error[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {

            return applicationService.applicationCreate(param);
        } catch (Exception ex) {
            LOGGER.debug("Exception:[{}]", ex);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("application create error [" + ex + "]");
            exception.setUserMessage("应用创建失败[" + ex + "]");
            exception.setUserTitle("应用创建失败");
            throw exception;
        }
    }
}
