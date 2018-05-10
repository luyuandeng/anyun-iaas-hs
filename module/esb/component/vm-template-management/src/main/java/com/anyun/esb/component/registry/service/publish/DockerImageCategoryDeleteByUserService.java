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
 * Created by gaopeng on 16-7-7.
 */
public class DockerImageCategoryDeleteByUserService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageCategoryDeleteByUserService.class);
    private RegistryService registryService;

    public DockerImageCategoryDeleteByUserService() {
        registryService = new RegistryServiceImpl();
    }

    @Override
    public String getName() {
        return "images_delete_category";
    }

    @Override
    public String getDescription() {
        return "Docker Image Category Delete ByUser Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId",String.class);

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
        List<String> ids = Arrays.asList(id.split(","));
        try {
            registryService.deleteUserDockerImageCategories(ids);
            return new Status<String>("success");
        } catch (Exception ex) {
            ex.printStackTrace();
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Delete ImageCategory error [" + ex.getMessage() + "]");
            exception.setUserMessage("镜像分类删除失败[" + ex.getMessage() + "]");
            exception.setUserTitle("镜像分类删除失败");
            throw exception;
        }
    }
}
