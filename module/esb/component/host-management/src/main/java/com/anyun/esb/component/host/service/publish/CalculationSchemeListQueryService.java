package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.CalculationSchemeService;
import com.anyun.esb.component.host.service.docker.impl.CalculationSchemeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CalculationSchemeListQueryService extends AbstractBusinessService {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CalculationSchemeListQueryService.class);
    private CalculationSchemeService calculationSchemeService;
    public CalculationSchemeListQueryService(){
        calculationSchemeService = new CalculationSchemeServiceImpl();
    }

    @Override
    public String getName() {
        return "calculationScheme_list_query";
    }

    @Override
    public String getDescription() {
        return "CalculationSchemeListQueryService";
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

        try{
            List<CalculationSchemeDto> list = calculationSchemeService.queryCalculationSchemeList(userUniqueId);
            if (list == null)
                return new ArrayList<>();
            return list;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("计算方案查询失败");
            throw exception;
        }
    }
}
