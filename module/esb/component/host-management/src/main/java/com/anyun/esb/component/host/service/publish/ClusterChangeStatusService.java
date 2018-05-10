package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.ClusterDto;
import com.anyun.cloud.param.ClusterChangeStatusParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
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
public class ClusterChangeStatusService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClusterChangeStatusService.class);
    private ClusterService clusterService;
    private ClusterDao clusterDao;

    public ClusterChangeStatusService() {
        clusterService = new ClusterServiceImpl();
        clusterDao = new ClusterDaoImpl();
    }


    @Override
    public String getName() {
        return "cluster_change_status";
    }

    @Override
    public String getDescription() {
        return "Change Cluster Status";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        if (StringUtils.isEmpty(postBody)) {
            LOGGER.debug("postBody is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        ClusterChangeStatusParam param = JsonUtil.fromJson(ClusterChangeStatusParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(), "DELETE");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        ClusterDto dto = clusterDao.selectClusterById(param.getId());
        if (dto == null) {
            LOGGER.debug("ClusterDto is null");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("ClusterDto is null");
            exception.setUserMessage("ClusterDto is null");
            exception.setUserTitle("集群不存在");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getStatus())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("status empty [" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (!param.getStatus().equals("Enable") && !param.getStatus().equals("Disable")) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("status format error[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            clusterService.changeClusterStatus(param);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("change cluster status error [" + e + "]");
            exception.setUserMessage("更改集群状态失败[" + e + "]");
            exception.setUserTitle("更改集群状态失败");
            throw exception;
        }
    }
}
