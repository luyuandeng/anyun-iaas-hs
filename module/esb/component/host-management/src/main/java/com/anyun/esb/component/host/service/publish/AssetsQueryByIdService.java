package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.AssetsService;
import com.anyun.esb.component.host.service.docker.impl.AssetsServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

public class AssetsQueryByIdService extends AbstractBusinessService {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AssetsQueryByIdService.class);
    private AssetsService assetsService;

    public AssetsQueryByIdService(){
        assetsService = new AssetsServiceImpl();
    }




    @Override
    public String getName() {
        return "assets_query_by_id";
    }

    @Override
    public String getDescription() {
        return "Assets Query By Id";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
//
//        String userUniqueId= exchange.getIn().getHeader("userUniqueId",String.class);
//        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
//        try {
//            UUIDVerify.userRightsVerification(userUniqueId,"GET");
//        } catch (Exception  e) {
//            JbiComponentException exception = new JbiComponentException(2000, 1000);
//            exception.setUserTitle("User rights validation failed");
//            exception.setUserMessage(e.getMessage());
//            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
//            throw exception;
//        }
        String id= exchange.getIn().getHeader("id",String.class);
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("Assets Id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Assets Id is empty");
            exception.setUserMessage("Assets Id is  empty");
            exception.setUserTitle("Assets param error");
            throw exception;
        }
        try {
            AssetsDto dto = assetsService.QueryAssetsInfo(id);
            if (dto == null)
                return new AssetsDto();
            return dto;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("资产管理查询失败败");
            throw exception;
        }

    }
}
