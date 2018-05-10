package com.anyun.esb.component.registry.service.publish;

import com.anyun.cloud.param.CommonQueryParam;
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
 * Created by gp on 17-5-12.
 */
public class DockerImageQueryListService  extends AbstractBusinessService{
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImageQueryListService.class);
    private RegistryService registryService;

    public DockerImageQueryListService() {
        registryService = new RegistryServiceImpl();
    }


    @Override
    public String getName() {
        return "images_query_list";
    }

    @Override
    public String getDescription() {
        return "Docker Image Query Message List";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String s = exchange.getIn().getHeader("s",String.class);
        LOGGER.debug("s:[{}]",s);
        CommonQueryParam commonQueryParam = JsonUtil.fromJson(CommonQueryParam.class, s);
        try {
            UUIDVerify.userRightsVerification(commonQueryParam.getUserUniqueId(),"QUERY");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        try {

            return  registryService.queryImageList(commonQueryParam);
        }catch (Exception e){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Registry  error [" + e.getMessage() + "]");
            exception.setUserMessage("镜像信息查询失败[" + e.getMessage() + "]");
            exception.setUserTitle("镜像信息查询失败");
            throw exception;

        }
    }
}
