package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.param.ContainerCreateByConditionParam;
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

/**
 * Created by sxt on 16-9-12.
 */
public class ContainerCreateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerCreateService.class);
    private ContainerService containerService;
    private JbiComponentException exception = null;

    public ContainerCreateService() {
        containerService = new ContainerServiceImpl();
    }

    @Override
    public String getName() {
        return "container_create";
    }

    @Override
    public String getDescription() {
        return "ContainerCreateByConditionService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        if (StringUtils.isEmpty(postBody)) {
            exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        ContainerCreateByConditionParam param = JsonUtil.fromJson(ContainerCreateByConditionParam.class, postBody);

        if (param == null) {
            exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is null");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            return containerService.createDefaultContainer(param);
        } catch (Exception ex) {
            exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Container create error [" + ex.getMessage() + "]");
            exception.setUserMessage("容器创建失败[" + ex.getMessage() + "]");
            exception.setUserTitle("容器创建失败");
            throw exception;
        }
    }
}
