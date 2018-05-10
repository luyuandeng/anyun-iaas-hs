package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.CalculationSchemeService;
import com.anyun.esb.component.host.service.docker.impl.CalculationSchemeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

public class CalculationSchemeQueryByIdService extends AbstractBusinessService {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CalculationSchemeQueryByIdService.class);
    private CalculationSchemeService calculationSchemeService;
    public CalculationSchemeQueryByIdService(){
        calculationSchemeService = new CalculationSchemeServiceImpl();
    }
    @Override
    public String getName() {
        return "calculationScheme_query_by_id";
    }

    @Override
    public String getDescription() {
        return "CalculationSchemeQueryByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String id= exchange.getIn().getHeader("id",String.class);
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("CalculationScheme Id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("CalculationScheme Id is empty");
            exception.setUserMessage("CalculationScheme Id is  empty");
            exception.setUserTitle("CalculationScheme param error");
            throw exception;
        }

        try{
            CalculationSchemeDto calculationSchemeDto = calculationSchemeService.queryCalculationSchemeInfo(id);
            if(calculationSchemeDto == null)
                return new CalculationSchemeDto();
            return calculationSchemeDto;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("计算方案查询失败败");
            throw exception;
        }
    }
}
