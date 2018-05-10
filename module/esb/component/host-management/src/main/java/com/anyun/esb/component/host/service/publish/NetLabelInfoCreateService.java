package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.NetLabelInfoDto;
import com.anyun.cloud.dto.ProjectDto;
import com.anyun.cloud.param.NetLabelInfoCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.NetLabelInfoDao;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.impl.NetLabelInfoDaoImpl;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.service.docker.NetService;
import com.anyun.esb.component.host.service.docker.impl.NetServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-7-22.
 */
public class NetLabelInfoCreateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NetLabelInfoCreateService.class);
    private NetService netService;
    private NetLabelInfoDao netLabelInfoDao;
    private ProjectDao projectDao;

    public NetLabelInfoCreateService() {
        netService = new NetServiceImpl();
        projectDao = new ProjectDaoImpl();
        netLabelInfoDao = new NetLabelInfoDaoImpl();
    }

    @Override
    public String getName() {
        return "netLabelInfo_create";
    }

    @Override
    public String getDescription() {
        return "NetLabelInfo Create Service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            throw exception;
        }

        NetLabelInfoCreateParam param = JsonUtil.fromJson(NetLabelInfoCreateParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is null");
            throw exception;
        }


        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"PUT");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getSubnet())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("params[subnet] is empty");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getProject())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("params[project] is empty");
            throw exception;
        }

        ProjectDto projectDto = projectDao.selectProjectById(param.getProject());
        if (projectDto == null || StringUtils.isEmpty(projectDto.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Project  does  not exist");
            throw exception;
        }

        if (StringUtils.isNotEmpty(projectDto.getPlatFormNetworkId())) {
            String label = projectDto.getPlatFormNetworkId();
            String type = "openvswitch";
            NetLabelInfoDto netLabelInfoDto = netLabelInfoDao.selectNetLabelInfoByLabelAndType(label, type);
            if (netLabelInfoDto != null && StringUtils.isNotEmpty(netLabelInfoDto.getLabel())) {
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("Each project can only has one network label");
                throw exception;
            }
        }

        try {
            param.setGateway("");//网关设为空
            netService.insertNetLabelInfo(param);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("NetLabelInfo   Insert   error " + e.getMessage());
            exception.setUserMessage("标签创建失败" + e.getMessage());
            exception.setUserTitle("标签创建失败");
            throw exception;
        }
    }
}
