package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
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

/**
 * Created by gp on 17-3-28.
 */
public class ClusterDeleteService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClusterDeleteService.class);
    private ClusterService clusterService;
    private ClusterDao clusterDao;

    public ClusterDeleteService() {
        clusterService = new ClusterServiceImpl();
        clusterDao = new ClusterDaoImpl();
    }

    @Override
    public String getName() {
        return "cluster_delete";
    }

    @Override
    public String getDescription() {
        return "Cluster Delete";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "DELETE");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        String id = exchange.getIn().getHeader("id", String.class);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("is Id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Id is empty");
            exception.setUserMessage("Id is  empty");
            exception.setUserTitle("Id is  empty");
            throw exception;
        }

        try {
            ClusterDto dto = clusterDao.selectClusterById(id);
            if(dto!=null){
                clusterService.deleteCluster(id);
            }
            return new Status<String>("success");
        } catch (Exception e) {
            LOGGER.debug("Exception:[{}]", e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("cluster delete error [" + e + "]");
            exception.setUserMessage("集群删除失败[" + e + "]");
            exception.setUserTitle("集群删除失败");
            throw exception;
        }
    }
}
