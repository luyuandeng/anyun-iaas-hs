package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ClusterDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.ClusterDao;
import com.anyun.esb.component.host.dao.impl.ClusterDaoImpl;
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
public class ClusterQueryListByStatusService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterQueryListByStatusService.class);
    private ClusterService clusterService;

    public ClusterQueryListByStatusService() {
        clusterService = new ClusterServiceImpl();
    }

    @Override
    public String getName() {
        return "cluster_query_list_by_status";
    }

    @Override
    public String getDescription() {
        return "ClusterQueryListService";
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
        String status = exchange.getIn().getHeader("status", String.class);
        if (StringUtils.isEmpty(status)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("status is empty");
            exception.setUserMessage("status is empty");
            exception.setMessage("status is empty");
            throw exception;

        }
        try {
            List<ClusterDto> l = clusterService.queryClusterByStatus(status);
            if (l == null)
                return new ArrayList<ClusterDto>();
            return l;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("cluster query fail[" + e.getMessage() + "]");
            exception.setUserMessage("cluster query fail[" + e.getMessage() + "]");
            exception.setUserTitle(" ClusterQueryListByStatusService fail");
            throw exception;
        }
    }
}
