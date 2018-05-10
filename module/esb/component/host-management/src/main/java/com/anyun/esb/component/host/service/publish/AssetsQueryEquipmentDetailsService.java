package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.AssetsEquipmentDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.AssetsDao;
import com.anyun.esb.component.host.dao.impl.AssetsDaoImpl;
import com.anyun.esb.component.host.service.docker.AssetsService;
import com.anyun.esb.component.host.service.docker.impl.AssetsServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jt-workspace on 17-4-13.
 */
public class AssetsQueryEquipmentDetailsService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AssetsQueryEquipmentDetailsService.class);
    private AssetsService assetsService;

    public AssetsQueryEquipmentDetailsService(){
        assetsService = new AssetsServiceImpl();
    }

    @Override
    public String getName() {
        return "assets_equipment_query_by_serialNumber";
    }

    @Override
    public String getDescription() {
        return "assets equipment query by  serialNumber";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId= exchange.getIn().getHeader("userUniqueId",String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String serialNumber = exchange.getIn().getHeader("serialNumber",String.class);
        LOGGER.debug("id:[{}]",serialNumber);
        if (StringUtils.isEmpty(serialNumber)) {
            LOGGER.debug("Assets serialNumber is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Assets serialNumber is empty");
            exception.setUserMessage("Assets serialNumber is  empty");
            exception.setUserTitle("Assets param error");
            throw exception;
        }


        try{
            AssetsEquipmentDto assetsEquipmentDto =assetsService.queryEquipmentDetails(serialNumber);
            if(assetsEquipmentDto == null){
                return new AssetsEquipmentDto();
            }
            return assetsEquipmentDto;
        }
        catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("根据 serialNumber 查询 失败");
            throw exception;
        }
    }
}
