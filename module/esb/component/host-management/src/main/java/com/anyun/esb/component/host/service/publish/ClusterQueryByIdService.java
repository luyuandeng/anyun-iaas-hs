package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ClusterDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.ClusterService;
import com.anyun.esb.component.host.service.docker.impl.ClusterServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gp on 17-3-28.
 */
public class ClusterQueryByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterQueryByIdService.class);
    private ClusterService clusterService;

    public ClusterQueryByIdService() {
        clusterService = new ClusterServiceImpl();
    }

    @Override
    public String getName() {
        return "cluster_query_by_id";
    }

    @Override
    public String getDescription() {
        return "ClusterQueryByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
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
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("Cluster Id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Cluster Id is empty");
            exception.setUserMessage("Cluster Id is  empty");
            exception.setUserTitle("Cluster param error");
            throw exception;
        }

        try {
            ClusterDto clusterDto = clusterService.queryClusterById(id);
            if (clusterDto == null)
                return new ClusterDto();
            return clusterDto;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage(e.getMessage());
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("根据 Id 查询集群 失败");
            throw exception;
        }
    }
}
