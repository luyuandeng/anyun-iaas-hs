package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
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
 * Created by jt-workspace on 17-4-18.
 */
public class AsstesDeleteEquipmentService  extends AbstractBusinessService{
    private final static Logger LOGGER = LoggerFactory.getLogger(AsstesDeleteEquipmentService.class);
    private AssetsService assetsService;
    private AssetsDao assetsDao;

    public AsstesDeleteEquipmentService(){
        assetsService = new AssetsServiceImpl();
        assetsDao = new AssetsDaoImpl();
    }

    @Override
    public String getName() {
        return "assets_equipment_delete";
    }

    @Override
    public String getDescription() {
        return "assets equipment delete ";
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

        AssetsEquipmentDto dto = assetsDao.queryEquipmentDetails(serialNumber);
        if (dto == null){
            LOGGER.debug("Assets Equipment is not exit");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Assets Equipment is not exit");
            exception.setUserMessage("Assets Equipment is not exit");
            exception.setUserTitle("Assets Equipment is not exit");
            throw exception;
        }
        try{
            assetsService.deleteEquipmentByserialNumber(serialNumber);
            return new Status<String>("success");
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("刪除 assets equipment 失敗");
            throw exception;
        }
    }
}
