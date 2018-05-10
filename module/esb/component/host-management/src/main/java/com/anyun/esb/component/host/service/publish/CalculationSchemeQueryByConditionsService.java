package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.CalculationSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.CalculationSchemeService;
import com.anyun.esb.component.host.service.docker.impl.CalculationSchemeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

public class CalculationSchemeQueryByConditionsService extends AbstractBusinessService {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CalculationSchemeQueryByConditionsService.class);
    private CalculationSchemeService calculationSchemeService;
    public CalculationSchemeQueryByConditionsService(){
        calculationSchemeService = new CalculationSchemeServiceImpl();
    }
    @Override
    public String getName() {
        return "calculationScheme_query_by_conditions";
    }

    @Override
    public String getDescription() {
        return "CalculationSchemeQueryByConditionsService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String s = exchange.getIn().getHeader("s",String.class);
        LOGGER.debug("s:[{}]",s);
        CommonQueryParam commonQueryParam = JsonUtil.fromJson(CommonQueryParam.class, s);
        try {
            UUIDVerify.userRightsVerification(commonQueryParam.getUserUniqueId(),"QUERY");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        try {
            PageDto<CalculationSchemeDto> calculationSchemeDtoPageDto = calculationSchemeService.pageQueryCalculationSchemeList(commonQueryParam);
            if(calculationSchemeDtoPageDto==null){
                return new PageDto<CalculationSchemeDto>();
            }
      return calculationSchemeDtoPageDto;
        } catch (Exception e) {

            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("计算方案查询失败" + e.getMessage());
            exception.setUserMessage("计算方案查询失败" + e.getMessage());
            exception.setUserTitle("计算方案查询失败");
            throw exception;
        }
    }
}
