package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.NetL2InfoDto;
import com.anyun.cloud.param.AreaCreateParam;
import com.anyun.cloud.param.NetL2CreateParam;
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
public class NetL2CreateService extends AbstractBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetL2InfoQueryByIdService.class);
    private NetL2InfoService netL2InfoService;
    public NetL2CreateService() {
        netL2InfoService = new NetL2InfoServiceImpl();
    }
    @Override
    public String getName() {
        return "netL2Info_create";
    }

    @Override
    public String getDescription() {
        return "NetL2CreateService";
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
        //转化参数实体类，验证参数
        NetL2CreateParam param = JsonUtil.fromJson(NetL2CreateParam.class,postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getName())){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("name is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getPhysical_interface())){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("physical_interface is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getType())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("type is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
           return   netL2InfoService.createNetL2(param);
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("area create error [" + e + "]");
            exception.setUserMessage("L2网络创建失败[" + e + "]");
            exception.setUserTitle("L2网络创建失败");
            throw exception;
        }
    }
}
