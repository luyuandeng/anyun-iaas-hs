package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.ContainerChangeCalculationSchemeParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContainerChangeCalculationSchemeService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerChangeCalculationSchemeService.class);
    private ContainerService containerService;

    public ContainerChangeCalculationSchemeService() {
        containerService = new ContainerServiceImpl();
    }

    @Override
    public String getName() {
        return "container_calculationScheme_change";
    }

    @Override
    public String getDescription() {
        return "ContainerChangeCalculationSchemeService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        JbiComponentException exception;
        String postBody = exchange.getIn().getBody(String.class);
        if (StringUtils.isEmpty(postBody)) {
            exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        ContainerChangeCalculationSchemeParam param = JsonUtil.fromJson(ContainerChangeCalculationSchemeParam.class, postBody);
        try {
            containerService.changeCalculationScheme(param);
            return new Status<String>("success");
        } catch (Exception ex) {
            exception = new JbiComponentException(2000, 1000);
            exception.setMessage("container change calculation scheme error[" + ex.getMessage() + "]");
            exception.setUserMessage("方案更新错误：[" + ex.getMessage() + "]");
            exception.setUserTitle("方案更新错误");
            throw exception;
        }
    }
}
