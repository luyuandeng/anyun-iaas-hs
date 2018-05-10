package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ClusterDto;
import com.anyun.cloud.param.ClusterCreateParam;
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

import java.util.List;

/**
 * Created by gp on 17-3-28.
 */
public class ClusterCreateService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ClusterCreateService.class);
    private ClusterService clusterService;
    private ClusterDao clusterDao;

    public ClusterCreateService() {
        clusterService = new ClusterServiceImpl();
        clusterDao = new ClusterDaoImpl();
    }

    @Override
    public String getName() {
        return "cluster_create";
    }

    @Override
    public String getDescription() {
        return "Cluster Ctreate";
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

        ClusterCreateParam param = JsonUtil.fromJson(ClusterCreateParam.class, postBody);
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

        if (StringUtils.isEmpty(param.getName())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("name is empty[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        //查询所有集群名称（验证参数用）
        List<ClusterDto> list = clusterDao.queryAllCluster();
        for (int i = 0; i < list.size(); i++) {
            ClusterDto dto = list.get(i);
            if (param.getName().equals(dto.getName())) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setUserMessage("Service param format error");
                exception.setMessage("name is exist[" + exception + "]");
                exception.setUserTitle("集群名称已存在");
                throw exception;
            }
        }
        if (!param.getStatus().equals("Enable") && !param.getStatus().equals("Disable")) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserMessage("Service param format error");
            exception.setMessage("status format error[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            return clusterService.createCluster(param);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("cluster create error [" + e + "]");
            exception.setUserMessage("集群创建失败[" + e + "]");
            exception.setUserTitle("集群创建失败");
            throw exception;
        }
    }
}
