package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.tools.StringUtils;
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
public class DatabaseDeleteByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseDeleteByIdService.class);
    private DatabaseService databaseService;

    public DatabaseDeleteByIdService() {
        databaseService = new DatabaseServiceImpl();
    }

    @Override
    public String getName() {
        return "database_delete_by_id";
    }

    @Override
    public String getDescription() {
        return "DatabaseDeleteByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        String id = exchange.getIn().getHeader("id", String.class);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            databaseService.deleteById(id);
            return new Status<String>("success");
        } catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("database delete error [" + ex.getMessage() + "]");
            exception.setUserMessage("database delete error [" + ex.getMessage() + "]");
            exception.setUserTitle("database delete error ");
            throw exception;
        }
    }
}
