package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.NetL2InfoDto;
import com.anyun.cloud.param.NetL2UpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.NetL2InfoService;
import com.anyun.esb.component.host.service.docker.impl.NetL2InfoServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/4/11.
 */
public class NetL2UpdateService extends AbstractBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetL2InfoQueryByIdService.class);
    private NetL2InfoService netL2InfoService;
    public NetL2UpdateService() {
        netL2InfoService = new NetL2InfoServiceImpl();
    }

    @Override
    public String getName() {
        return "netL2Info_update";
    }

    @Override
    public String getDescription() {
        return "NetL2UpdateService";
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
        NetL2UpdateParam param = JsonUtil.fromJson(NetL2UpdateParam.class,postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try{
            //验证区域是否存在
            NetL2InfoDto dto = netL2InfoService.queryNetL2InfoById(param.getId());
            if (dto == null){
                LOGGER.debug("NetL2InfoDto is null");
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("NetL2InfoDto is null");
                exception.setUserMessage("NetL2InfoDto is null");
                exception.setUserTitle("L2网络不存在");
                throw exception;
            }
            return netL2InfoService.updateNetL2(param);
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("area  error [" + e + "]");
            exception.setUserMessage("L2网络更新失败[" + e + "]");
            exception.setUserTitle("L2网络更新失败");
            throw exception;
        }
    }
}
