package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AssetsDto;
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
 * Created by twh-workspace on 17-4-11.
 */
public class AssetsDeleteService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetsDeleteService.class);
    private AssetsService assetsService;
    private AssetsDao assetsDao;

    public AssetsDeleteService() {
        assetsService = new AssetsServiceImpl();
        assetsDao = new AssetsDaoImpl();
    }

    @Override
    public String getName() {
        return "assets_base_delete";
    }

    @Override
    public String getDescription() {
        return "AssetsDeleteService";
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
        LOGGER.debug("serialNumber:[{}]", serialNumber);
        if (StringUtils.isEmpty(serialNumber)) {
            LOGGER.debug("Assets serialNumber is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Assets serialNumber is empty");
            exception.setUserMessage("Assets serialNumber is  empty");
            exception.setUserTitle("Assets param error");
            throw exception;
        }

        try {
            assetsService.deleteEquipmentByserialNumber(serialNumber);
            assetsService.deleteAssets(serialNumber);
            return new Status<String>("success");
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("assets delete error [" + e + "]");
            exception.setUserMessage("资产删除失败[" + e + "]");
            exception.setUserTitle("资产删除失败");
            throw exception;
        }
    }
}
