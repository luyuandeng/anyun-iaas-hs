package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ContainerOnHostDto;
import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-8-9.
 */
public class HostInfoQueryByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostInfoQueryByIdService.class);
    private HostBaseInfoDao hostBaseInfoDao;

    public HostInfoQueryByIdService() {
        hostBaseInfoDao = new HostBaseInfoDaoImpl();
    }

    @Override
    public String getName() {
        return "hostInfo_query_by_id";
    }

    @Override
    public String getDescription() {
        return "HostInfoQueryByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);

        try {
            UUIDVerify.userRightsVerification(userUniqueId, "GET");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String id = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("param is :" + id);
        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("param is empty");
            exception.setUserMessage("param  is  empty");
            exception.setMessage("param  is  empty");
            throw exception;
        }

        try {
            HostBaseInfoDto hDto = hostBaseInfoDao.selectHostInfoById(id);
            if (hDto == null)
                return new HostBaseInfoDto();
            ContainerService cService = new ContainerServiceImpl();
            ContainerOnHostDto cDto = cService.getContainerOnHostDtoByHost(id);
            if (cDto != null)
                hDto.setContainerOnHostDto(cDto);
            return hDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("HostInfoQuery  fail [" + e.getMessage() + "]");
            exception.setUserMessage("HostInfoQuery fail： [" + e.getMessage() + "]");
            exception.setUserTitle("HostInfoQuery fail");
            throw exception;
        }
    }
}
