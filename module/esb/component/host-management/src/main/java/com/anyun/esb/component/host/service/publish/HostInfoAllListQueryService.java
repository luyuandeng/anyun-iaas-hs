package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-8-8.
 */
public class HostInfoAllListQueryService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostInfoAllListQueryService.class);
    private HostBaseInfoDao hostBaseInfoDao;

    public HostInfoAllListQueryService() {
        hostBaseInfoDao = new HostBaseInfoDaoImpl();
    }

    @Override
    public String getName() {
        return "hostInfo_allList_query";
    }

    @Override
    public String getDescription() {
        return "HostInfoQueryService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        List<HostBaseInfoDto> dtos = hostBaseInfoDao.selectAllHostInfo();
        if (dtos == null) {
            dtos = new ArrayList<>();
        }
        return dtos;
    }
}
