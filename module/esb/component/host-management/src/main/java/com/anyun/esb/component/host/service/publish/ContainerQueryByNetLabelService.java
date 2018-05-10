package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.NetLabelInfoDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.NetLabelInfoDao;
import com.anyun.esb.component.host.dao.impl.NetLabelInfoDaoImpl;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-7-15.
 */
public class ContainerQueryByNetLabelService extends AbstractBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerQueryByNetLabelService.class);
    private ContainerService containerService;
    private NetLabelInfoDao netLabelInfoDao;

    public ContainerQueryByNetLabelService() {
        containerService = new ContainerServiceImpl();
        netLabelInfoDao = new NetLabelInfoDaoImpl();
    }

    @Override
    public String getName() {
        return "container_query_by_netLabel";
    }

    @Override
    public String getDescription() {
        return "Container Query By NetLabel Service";
    }


    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);

        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String id = exchange.getIn().getHeader("id", String.class);
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("id is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("param is  empty");
            throw exception;
        }
        NetLabelInfoDto netLabelInfoDto = netLabelInfoDao.selectNetLabelInfoByLabel(id);
        if (netLabelInfoDto == null) {
            LOGGER.debug("this netLabel is not exit");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("this netLabel  is not exit");
            exception.setUserMessage("this netLabel  is not exit");
            exception.setUserTitle("this netLabel  is not exit");
            throw exception;
        }

        try {
            LOGGER.debug("param:[{}]", id);
            List<ContainerDto> dtos = containerService.queryContainerByNetLabel(id, 0);
            if (dtos == null)
                return new ArrayList<ContainerDto>();
            return dtos;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("根据 网络Id 查询容器 失败");
            throw exception;
        }
    }
}

