package com.anyun.esb.component.registry.service.publish;

import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.registry.common.UUIDVerify;
import com.anyun.esb.component.registry.service.core.RegistryService;
import com.anyun.esb.component.registry.service.core.impl.RegistryImageServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.anyun.cloud.tools.StringUtils;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 17-3-7.
 */
public class DockerImageQueryService extends AbstractBusinessService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DockerImageQueryService.class);
    private RegistryService registryService;

    public DockerImageQueryService() {
        registryService = new RegistryImageServiceImpl();
    }

    @Override
    public String getName() {
        return "image_query";
    }

    @Override
    public String getDescription() {
        return "DockerImageQueryService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "GET");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String subMethod = exchange.getIn().getHeader("subMethod", String.class);
        LOGGER.debug("subMethod:[{}]", subMethod);
        if (StringUtils.isEmpty(subMethod)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("subMethod is empty");
            exception.setUserMessage("subMethod is  empty");
            exception.setUserTitle("subMethod param error");
            throw exception;
        }

        String subParameters = exchange.getIn().getHeader("subParameters", String.class);
        LOGGER.debug("subParameters:[{}]", subParameters);

        try {
            List<DockerImageDto> l = registryService.queryImage(subMethod, subParameters);
            if (l == null || l.isEmpty())
                return new ArrayList<DockerImageDto>();
            return l;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("image query fail: [" + e.getMessage() + "]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("查询 镜像 失败");
            throw exception;
        }
    }
}
