package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.CalculationSchemeService;
import com.anyun.esb.component.host.service.docker.impl.CalculationSchemeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

public class CalculationSchemeDeleteByIdService extends AbstractBusinessService{
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CalculationSchemeDeleteByIdService.class);
    private CalculationSchemeService calculationSchemeService;
    public CalculationSchemeDeleteByIdService(){
        calculationSchemeService = new CalculationSchemeServiceImpl();
    }

    @Override
    public String getName() {
        return "calculationScheme_delete_by_id";
    }

    @Override
    public String getDescription() {
        return "CalculationSchemeDeleteByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "DELETE");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        String id = exchange.getIn().getHeader("id", String.class);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("is Id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Id is empty");
            exception.setUserMessage("Id is  empty");
            exception.setUserTitle("Id is  empty");
            throw exception;
        }

        try {
            CalculationSchemeDto dto = calculationSchemeService.queryCalculationSchemeInfo(id);
            if(dto!=null){
                calculationSchemeService.deleteCalculationScheme(id);
            }
            return new Status<String>("success");
        } catch (Exception e) {
            LOGGER.debug("Exception:[{}]", e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("CalculationScheme delete error [" + e + "]");
            exception.setUserMessage("CalculationScheme删除失败[" + e + "]");
            exception.setUserTitle("CalculationScheme失败");
            throw exception;
        }
    }
}
