package com.anyun.esb.component.registry.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.ImageCategoryRegistParam;
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
public class DockerImageCategoryUpdateByUserService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageCategoryUpdateByUserService.class);
    private RegistryService registryService;

    public DockerImageCategoryUpdateByUserService() {
        registryService = new RegistryServiceImpl();
    }

    @Override
    public String getName() {
        return "images_update_category";
    }

    @Override
    public String getDescription() {
        return " Update Image Category Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {

        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        ImageCategoryRegistParam updateParam = JsonUtil.fromJson(ImageCategoryRegistParam.class, postBody);
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
        if (StringUtils.isEmpty(updateParam.getDescript())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service updateParam [descript] not exist");
            exception.setUserMessage("服务参数 [descript] 不存在");
            exception.setUserTitle("参数 descript 验证失败");
            throw exception;
        }
        if (StringUtils.isEmpty(updateParam.getShortName())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service updateParam [shortName] not exist");
            exception.setUserMessage("服务参数 [shortName] 不存在");
            exception.setUserTitle("参数 shortName 验证失败");
            throw exception;
        }



        try {
           return registryService.updateUserDockerImageCategories(updateParam);
        } catch (Exception ex) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Update ImageCategory error [" + ex.getMessage() + "]");
            exception.setUserMessage("镜像分类更新失败[" + ex.getMessage() + "]");
            exception.setUserTitle("镜像分类更新失败");
            throw exception;
        }

    }
}
