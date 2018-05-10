package com.anyun.esb.component.registry.service.publish;

import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.registry.common.UUIDVerify;
import com.anyun.esb.component.registry.service.core.PictureService;
import com.anyun.esb.component.registry.service.core.impl.PictureServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zhoubin on 2017/5/11.
 */
public class PictureQueryByConditionsService extends AbstractBusinessService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PictureQueryByConditionsService.class);
    private PictureService pictureService;

    public PictureQueryByConditionsService() {
        pictureService = new PictureServiceImpl();
    }

    @Override
    public String getName() {
        return "picture_query_by_conditions";
    }

    @Override
    public String getDescription() {
        return "PictureQueryByConditionsService";
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
            return pictureService.getPageListConditions(commonQueryParam);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("区域查询失败" + e.getMessage());
            exception.setUserMessage("区域查询失败" + e.getMessage());
            exception.setUserTitle("区域查询失败");
            throw exception;
        }
    }
}
