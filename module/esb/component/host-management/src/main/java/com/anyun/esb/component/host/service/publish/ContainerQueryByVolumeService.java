package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
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
 * Created by sxt on 16-9-12.
 */
public class ContainerQueryByVolumeService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerQueryByVolumeService.class);
    private ContainerService containerService;

    public ContainerQueryByVolumeService() {
        containerService = new ContainerServiceImpl();
    }

    @Override
    public String getName() {
        return "container_query_by_volume";
    }

    @Override
    public String getDescription() {
        return "ContainerQueryByVolumeService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId=exchange.getIn().getHeader("userUniqueId",String.class);
        LOGGER.debug("userUniqueId:[{}]",userUniqueId);

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
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("volume is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[id] is empty");
            exception.setUserMessage("服务参数 [id]  is  empty");
            exception.setUserTitle("参数 id 验证失败");
            throw exception;
        }
        try {
            LOGGER.debug("id:[{}]", id);
            List<ContainerDto> l = containerService.queryContainerForMountedVolume(id,0);
            if (l == null) {
                return new ArrayList<ContainerDto>();
            }
            return l;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Query container fail:[" + e.getMessage() + "]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("Query container fail");
            throw exception;
        }
    }
}

