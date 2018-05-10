package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.param.CalculationSchemeCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.CalculationSchemeService;
import com.anyun.esb.component.host.service.docker.impl.CalculationSchemeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

public class CalculationSchemeCreateService extends AbstractBusinessService{
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(CalculationSchemeCreateService.class);
    private CalculationSchemeService calculationSchemeService;
    public CalculationSchemeCreateService(){
        calculationSchemeService = new CalculationSchemeServiceImpl();
    }

    @Override
    public String getName() {
        return "calculationScheme_create";
    }

    @Override
    public String getDescription() {
        return "CalculationSchemeCreateService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            LOGGER.debug("postBody is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        CalculationSchemeCreateParam calculationSchemeCreateParam = JsonUtil.fromJson(CalculationSchemeCreateParam.class,postBody);
        LOGGER.debug("param:[{}]", calculationSchemeCreateParam.asJson());
        if (calculationSchemeCreateParam == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(calculationSchemeCreateParam.getName())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("name is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (StringUtils.isEmpty(calculationSchemeCreateParam.getDescription())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("Description is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (calculationSchemeCreateParam.getCpuShares()>262144 || calculationSchemeCreateParam.getCpuShares()<2){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("CpuShares is error[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (calculationSchemeCreateParam.getMemory()<4){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("Memory is error[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try{
            return calculationSchemeService.createCalculationScheme(calculationSchemeCreateParam);
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("CalculationScheme create error [" + e + "]");
            exception.setUserMessage("CalculationScheme创建失败[" + e + "]");
            exception.setUserTitle("CalculationScheme创建失败");
            throw exception;
        }
    }
}
