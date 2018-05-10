package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.DatabaseDto;
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
public class DatabaseDetailsQueryByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseDetailsQueryByIdService.class);
    private DatabaseService databaseService;

    public DatabaseDetailsQueryByIdService() {
        databaseService = new DatabaseServiceImpl();
    }

    @Override
    public String getName() {
        return "database_details_query_by_id";
    }

    @Override
    public String getDescription() {
        return "DatabaseDetailsQueryByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        String id = exchange.getIn().getHeader("id", String.class);

        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("id is empty");
            exception.setUserMessage("id is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            DatabaseDto databaseDto = databaseService.getDatabaseDtoById(id,userUniqueId);
            if (databaseDto == null)
                return new DatabaseDto();
            return databaseDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("database query fail");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("database query fail:{" + e.getMessage() + "}");
            throw exception;
        }
    }
}
