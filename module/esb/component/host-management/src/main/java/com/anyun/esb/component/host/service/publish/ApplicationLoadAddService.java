package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ApplicationInfoLoadDto;
import com.anyun.cloud.param.ApplicationLoadAddParam;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 17-6-2.
 */
public class ApplicationLoadAddService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationLoadAddService.class);
    private ApplicationService applicationService;

    public ApplicationLoadAddService() {
        applicationService = new ApplicationServiceImpl();
    }

    @Override
    public String getName() {
        return "load_add";
    }

    @Override
    public String getDescription() {
        return "ApplicationLoadAddService";
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

        ApplicationLoadAddParam param = JsonUtil.fromJson(ApplicationLoadAddParam.class, postBody);
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


        if (StringUtils.isEmpty(param.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("id  is  empty");
            exception.setMessage("id  is  empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (param.getAmount()<1) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("amount < 1");
            exception.setMessage("amount < 1["+ exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            List<ApplicationInfoLoadDto> l= applicationService.addLoad(param.getId(),param.getAmount());
            if(l==null)
                l=new ArrayList<>();
            return l;
        } catch (Exception ex) {
            LOGGER.debug("Exception:[{}]", ex);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("load create error [" + ex + "]");
            exception.setUserMessage("负载[" + ex + "]");
            exception.setUserTitle("负载创建失败");
            throw exception;
        }
    }
}
