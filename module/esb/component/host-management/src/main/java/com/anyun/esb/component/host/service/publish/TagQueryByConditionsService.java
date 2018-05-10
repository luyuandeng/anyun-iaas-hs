package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.TagService;
import com.anyun.esb.component.host.service.docker.impl.TagServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by admin on 2017/5/9.
 */
public class TagQueryByConditionsService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TagQueryByConditionsService.class);
    private TagService tagService;

    public TagQueryByConditionsService() {
        tagService = new TagServiceImpl();
    }
    @Override
    public String getName() {
        return "tag_query_by_conditions";
    }

    @Override
    public String getDescription() {
        return "TagQueryByConditions";
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
            return tagService.queryByConditions(commonQueryParam);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("TAG查询失败" + e.getMessage());
            exception.setUserMessage("TAG查询失败" + e.getMessage());
            exception.setUserTitle("TAG查询失败");
            throw exception;
        }
    }
}
