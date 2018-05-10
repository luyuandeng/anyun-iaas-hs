package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.param.ContainerStopParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-6-13.
 */
public class ContainerStopService extends AbstractBusinessService {
    private ContainerService containerService;
    private ContainerDao containerDao;

    public ContainerStopService() {
        containerService = new ContainerServiceImpl();
        containerDao = new ContainerDaoImpl();
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerStopService.class);

    @Override
    public String getName() {
        return "container_stop_by_id";
    }

    @Override
    public String getDescription() {
        return "container.stop.by_id";
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

        ContainerStopParam param = JsonUtil.fromJson(ContainerStopParam.class, postBody);
        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(), "POST");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getId())) {
            LOGGER.debug("id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("id is empty");
            exception.setUserMessage("id is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        ContainerDto containerDto = containerDao.selectContainerById(param.getId());
        if (containerDto == null) {
            LOGGER.debug("containerDto is null");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("containerDto is null");
            exception.setUserMessage("containerDto is null");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            LOGGER.debug("param:[{}]", param.getId());
            containerService.stopContainer(param.getId());
            return new Status<String>("success");
        } catch (Exception ex) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("container  stop  error  info[" + ex.getMessage() + "]");
            exception.setUserMessage("容器停止错误信息 [" + ex.getMessage() + "]");
            exception.setUserTitle("容器停止错误");
            throw exception;
        }
    }
}
