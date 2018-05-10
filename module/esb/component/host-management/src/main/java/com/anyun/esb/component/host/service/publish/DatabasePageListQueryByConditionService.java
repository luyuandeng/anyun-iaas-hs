package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.DatabaseService;
import com.anyun.esb.component.host.service.docker.impl.DatabaseServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 7/4/17.
 */
public class DatabasePageListQueryByConditionService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabasePageListQueryByConditionService.class);
    private DatabaseService databaseService;

    public DatabasePageListQueryByConditionService() {
        databaseService = new DatabaseServiceImpl();
    }

    @Override
    public String getName() {
        return "database_pageList_query_by_condition";
    }

    @Override
    public String getDescription() {
        return "DatabasePageListQueryByConditionService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String s = exchange.getIn().getHeader("s", String.class);
        LOGGER.debug("s:[{}]", s);
        CommonQueryParam commonQueryParam = JsonUtil.fromJson(CommonQueryParam.class, s);

        try {
            return databaseService.getPageDtoByCondition(commonQueryParam);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("查询失败" + e.getMessage());
            exception.setUserMessage("查询失败" + e.getMessage());
            exception.setUserTitle("查询失败");
            throw exception;
        }
    }
}
