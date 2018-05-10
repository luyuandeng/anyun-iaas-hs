package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.AssetsDto;
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

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jt-workspace on 17-4-13.
 */
public class AssetsQueryEquipmentListByCategory extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AssetsQueryEquipmentListByCategory.class);
    private AssetsService assetsService;

    public AssetsQueryEquipmentListByCategory() {
        assetsService = new AssetsServiceImpl();
    }

    @Override
    public String getName() {
        return "assets_equipment_list_query";
    }

    @Override
    public String getDescription() {
        return "assets query equipment list by category";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);

        try {
            UUIDVerify.userRightsVerification(userUniqueId, "GET");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }


        String category = exchange.getIn().getHeader("category", String.class);
        LOGGER.debug("id:[{}]", category);
        if (StringUtils.isEmpty(category)) {
            LOGGER.debug("Assets category is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Assets category is empty");
            exception.setUserMessage("Assets category is  empty");
            exception.setUserTitle("Assets param error");
            throw exception;
        }


        try{
            List<AssetsDto> list = assetsService.queryAssetsByCategory(category);
            List<AssetsEquipmentDto> dto =new ArrayList<>();

            for(int i = 0 ; i < list.size() ; i++) {
                String serialNumber =list.get(i).getSerialNumber();
                dto.add(assetsService.queryEquipmentDetails(serialNumber));
            }
            if (dto==null)
                return null;
            return dto;
        }
        catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("根据 category 查询 失败");
            throw exception;
        }
    }
}
