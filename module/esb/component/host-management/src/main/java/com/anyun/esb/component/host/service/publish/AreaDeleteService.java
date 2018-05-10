package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AreaDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.AreaService;
import com.anyun.esb.component.host.service.docker.impl.AreaServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gp on 17-3-28.
 */
public class AreaDeleteService  extends AbstractBusinessService{
    private final static Logger LOGGER = LoggerFactory.getLogger(AreaDeleteService.class);
    private AreaService areaService;

    public AreaDeleteService(){
        areaService = new AreaServiceImpl();
    }


    @Override
    public String getName() {
        return "area_delete";
    }

    @Override
    public String getDescription() {
        return "Area Delete";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId= exchange.getIn().getHeader("userUniqueId",String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"DELETE");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        String id= exchange.getIn().getHeader("id",String.class);
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("Area Id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Area Id is empty");
            exception.setUserMessage("Area Id is  empty");
            exception.setUserTitle("Area param error");
            throw exception;
        }

        try{
            //验证区域是否存在
            AreaDto dto = areaService.queryById(id);
            if (dto == null){
                LOGGER.debug("AreaDto is null");
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("AreaDto is null");
                exception.setUserMessage("AreaDto is null");
                exception.setUserTitle("区域不存在");
                throw exception;
            }
            areaService.deleteArea(id);
            return new Status<String>("success");
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("area  error [" + e + "]");
            exception.setUserMessage("区域删除失败[" + e + "]");
            exception.setUserTitle("区域删除失败");
            throw exception;
        }
    }
}
