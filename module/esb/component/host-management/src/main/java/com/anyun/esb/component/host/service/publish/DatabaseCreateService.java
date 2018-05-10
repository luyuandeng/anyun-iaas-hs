package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.DatabaseDto;
import com.anyun.cloud.param.DatabaseCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
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
public class DatabaseCreateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseCreateParam.class);
    private DatabaseService databaseService;

    public DatabaseCreateService() {
        databaseService = new DatabaseServiceImpl();
    }

    @Override
    public String getName() {
        return "database_create";
    }

    @Override
    public String getDescription() {
        return "DatabaseCreateService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        DatabaseCreateParam param = JsonUtil.fromJson(DatabaseCreateParam.class, postBody);
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            DatabaseDto databaseDto = databaseService.createDatabase(param);
            if (databaseDto == null)
                databaseDto = new DatabaseDto();
            return databaseDto;

        } catch (Exception ex) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("database create fail [" + ex + "]");
            exception.setUserMessage("database create fail [" + ex + "]");
            exception.setUserTitle("database create fail ");
            throw exception;
        }
    }
}
