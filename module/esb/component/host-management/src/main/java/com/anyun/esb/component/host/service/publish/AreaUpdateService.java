package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AreaDto;
import com.anyun.cloud.param.AreaCreateParam;
import com.anyun.cloud.param.AreaUpdateParam;
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
 * Created by gp on 17-3-27.
 */
public class AreaUpdateService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AreaUpdateService.class);
    private AreaService areaService;

    public AreaUpdateService(){
        areaService = new AreaServiceImpl();
    }

    @Override
    public String getName() {
        return "area_update";
    }

    @Override
    public String getDescription() {
        return "Area Update";
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
        AreaUpdateParam param = JsonUtil.fromJson(AreaUpdateParam.class,postBody);
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
            AreaDto dto = areaService.queryById(param.getId());
            if (dto == null){
                LOGGER.debug("AreaDto is null");
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("AreaDto is null");
                exception.setUserMessage("AreaDto is null");
                exception.setUserTitle("区域不存在");
                throw exception;
            }
            areaService.updateArea(param);
            return new Status<String>("success");
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("area  error [" + e + "]");
            exception.setUserMessage("区域更新失败[" + e + "]");
            exception.setUserTitle("区域更新失败");
            throw exception;
        }
    }
}
