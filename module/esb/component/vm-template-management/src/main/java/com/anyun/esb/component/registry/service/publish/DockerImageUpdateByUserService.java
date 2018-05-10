package com.anyun.esb.component.registry.service.publish;
import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.ImageRegistParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.registry.common.UUIDVerify;
import com.anyun.esb.component.registry.service.core.RegistryService;
import com.anyun.esb.component.registry.service.core.impl.RegistryServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gaopeng on 16-7-7.
 */
public class DockerImageUpdateByUserService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageUpdateByUserService.class);

    private RegistryService registryService;

    public DockerImageUpdateByUserService() {
        registryService = new RegistryServiceImpl();
    }

    @Override
    public String getName() {
        return "images_update";
    }

    @Override
    public String getDescription() {
        return "Docker Image Update ByUser Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {

        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        ImageRegistParam updateParam = JsonUtil.fromJson(ImageRegistParam.class, postBody);
        if (updateParam == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            UUIDVerify.userRightsVerification(updateParam.getUserUniqueId(),"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }


        if (StringUtils.isEmpty(updateParam.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service updateParam [id] not exist");
            exception.setUserMessage("服务参数 [id] 不存在");
            exception.setUserTitle("参数 id 验证失败");
            throw exception;
        }
        if (StringUtils.isEmpty(updateParam.getUserUniqueId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("userUniqueId is empty");
            exception.setUserMessage("userUniqueId is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }


        try {
            return registryService.updateUserDockerImage(updateParam);

        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service update error");
            exception.setUserMessage("服务更新失败");
            exception.setUserTitle("失败");
            throw exception;
        }

    }

}
