package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.AreaCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.AreaService;
import com.anyun.esb.component.host.service.docker.impl.AreaServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gp on 17-3-23.
 */
public class AreaCreateService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AreaCreateService.class);
    private AreaService areaService;

    public AreaCreateService(){
        areaService = new AreaServiceImpl();
    }

    @Override
    public String getName() {
        return "area_create";
    }

    @Override
    public String getDescription() {
        return "Area Create";
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
        AreaCreateParam param = JsonUtil.fromJson(AreaCreateParam.class,postBody);
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

        if (StringUtils.isEmpty(param.getType())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("type is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            areaService.createArea(param);
            return new Status<String>("success");
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("area create error [" + e + "]");
            exception.setUserMessage("区域创建失败[" + e + "]");
            exception.setUserTitle("区域创建失败");
            throw exception;
        }
    }
}
