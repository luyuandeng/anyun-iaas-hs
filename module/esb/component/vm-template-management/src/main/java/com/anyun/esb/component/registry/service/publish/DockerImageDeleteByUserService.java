package com.anyun.esb.component.registry.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.registry.common.UUIDVerify;
import com.anyun.esb.component.registry.service.core.RegistryService;
import com.anyun.esb.component.registry.service.core.impl.RegistryServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hwt on 16-7-7.
 */
public class DockerImageDeleteByUserService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageDeleteByUserService.class);
    private RegistryService registry;

    public DockerImageDeleteByUserService() {
         registry = new RegistryServiceImpl();
    }

    @Override
    public String getName() {
        return "images_delete_docker";
    }

    @Override
    public String getDescription() {
        return "Docker Image Delete ByUser  Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId= exchange.getIn().getHeader("userUniqueId",String.class);

        try {
            UUIDVerify.userRightsVerification(userUniqueId,"DELETE");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String id = exchange.getIn().getHeader("ids", String.class);
        LOGGER.debug("id:[{}]", id);
        try {
            List<String> ids = Arrays.asList(id.split(","));
            LOGGER.debug("ids:[{}]", id);
            registry.deleteUserDockerImage(ids);
            return new Status<String>("success");
        } catch (Exception e) {
            e.printStackTrace();
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Delete Docker  error");
            exception.setUserMessage("删除用户 镜像 失败[" + e.getMessage() + "]");
            exception.setUserTitle("删除用户 镜像 失败");
            throw exception;
        }
    }
}
