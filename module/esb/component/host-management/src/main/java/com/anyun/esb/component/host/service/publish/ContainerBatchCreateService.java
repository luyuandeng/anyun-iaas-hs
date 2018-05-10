package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.param.ContainerCreateByConditionParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/11/10.
 */
public class ContainerBatchCreateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerBatchCreateService.class);
    private ContainerService containerService;
    private ProjectDao projectDao;

    public ContainerBatchCreateService() {
        containerService = new ContainerServiceImpl();
        projectDao = new ProjectDaoImpl();
    }

    @Override
    public String getName() {
        return "container_batch_create";
    }

    @Override
    public String getDescription() {
        return "ContainerBatchCreateService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        List<Map> list = JsonUtil.fromJson(List.class, postBody);
        List<ContainerCreateByConditionParam> params = new ArrayList<>();
        for (Map map : list) {
            if (map.isEmpty())
                continue;
            ContainerCreateByConditionParam a = JsonUtil.fromJson(ContainerCreateByConditionParam.class, JsonUtil.toJson(map));
            params.add(a);
        }
        if (params == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (params.size() == 0) {
            return new ArrayList<>();
        }

        try {
            List<ContainerDto> l = containerService.batchCreate(params);
            if (l == null)
                l = new ArrayList<>();
            return l;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
