package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.param.AssetsUpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.AssetsService;
import com.anyun.esb.component.host.service.docker.impl.AssetsServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetsUpdateService extends AbstractBusinessService{
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetsUpdateService.class);
    public AssetsUpdateService(){
        assetsService = new AssetsServiceImpl();
    }
    private AssetsService assetsService;
    @Override
    public String getName() {
        return "assets_update";
    }

    @Override
    public String getDescription() {
        return "assets_update Service";
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
        AssetsUpdateParam param = JsonUtil.fromJson(AssetsUpdateParam.class,postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try{
            //验证Assets是否存在
            AssetsDto dto = assetsService.QueryAssetsInfo(param.getId());
            if (dto == null){
                LOGGER.debug("AssetsDto is null");
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("AssetsDto is null");
                exception.setUserMessage("AssetsDto is null");
                exception.setUserTitle("Assets不存在");
                throw exception;
            }
            return assetsService.updateAsserts(param);
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Assets  error [" + e + "]");
            exception.setUserMessage("Assets更新失败[" + e + "]");
            exception.setUserTitle("Assets更新失败");
            throw exception;
        }
    }
}
