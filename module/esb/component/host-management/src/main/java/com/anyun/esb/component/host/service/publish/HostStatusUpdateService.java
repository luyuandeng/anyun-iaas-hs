package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.HostTailDto;
import com.anyun.cloud.param.HostStatusUpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.service.docker.HostService;
import com.anyun.esb.component.host.service.docker.impl.HostServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zy on 17-4-17.
 */
public class HostStatusUpdateService extends AbstractBusinessService {
    private static final Logger LOGGER= LoggerFactory.getLogger(HostStatusUpdateService.class);
    HostService hostService;

    public HostStatusUpdateService() {
        hostService = new HostServiceImpl();
    }

    @Override
    public String getName() {
        return "host_status_update";
    }

    @Override
    public String getDescription() {
        return "host status update service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody=exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            LOGGER.debug("postBody is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        HostStatusUpdateParam param= JsonUtil.fromJson(HostStatusUpdateParam.class,postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        try {
            HostTailDto dto=hostService.selectHostInfoByDescription(param.getHostip());
            if (dto == null){
                LOGGER.debug("HostTailDto is null");
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setMessage("HostTailDto is null");
                exception.setUserMessage("HostTailDto is null");
                exception.setUserTitle("宿主机不存在");
                throw exception;
            }
            hostService.updateHostStatus(param);
            return new Status<String>("success");
        }catch (Exception e){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Host update fail：[{" + e.getMessage() + "}]");
            exception.setUserMessage("Host update fail");
            exception.setUserTitle("Host update fail");
            throw exception;
        }
    }
}
