package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.AssetsService;
import com.anyun.esb.component.host.service.docker.impl.AssetsServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

public class AssetsQueryByConditionsService extends AbstractBusinessService {

    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AssetsQueryByConditionsService.class);
    private AssetsService assetsService;

    public AssetsQueryByConditionsService(){
        assetsService = new AssetsServiceImpl();
    }



    @Override
    public String getName() {
        return "assets_query_by_conditions";
    }

    @Override
    public String getDescription() {
        return null;
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
            return assetsService.QueryAssetsByCondition(commonQueryParam);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("资产管理查询失败" + e.getMessage());
            exception.setUserMessage("资产管理查询失败" + e.getMessage());
            exception.setUserTitle("资产管理查询失败");
            throw exception;
        }
    }
}
