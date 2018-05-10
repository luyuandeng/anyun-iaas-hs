package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.HostTailDto;
import com.anyun.cloud.param.HostCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.HostService;
import com.anyun.esb.component.host.service.docker.impl.HostServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by zy on 17-3-28.
 */
public class HostCreateService extends AbstractBusinessService {
    private static final Logger LOGGER= LoggerFactory.getLogger(HostCreateService.class);
    private HostService hostService;

    public HostCreateService(){hostService=new HostServiceImpl();}

    @Override
    public String getName() {
        return "host_create";
    }

    @Override
    public String getDescription() {
        return "host create service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String body=exchange.getIn().getBody(String.class);
        LOGGER.debug("body:[{}]",body);
        if (StringUtils.isEmpty(body)){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("body is empty");
            exception.setUserMessage("body is empty");
            exception.setUserTitle("body is empty");
            throw exception;
        }
        HostCreateParam param;
        try {
            param = JsonUtil.fromJson(HostCreateParam.class, body);
            LOGGER.debug("param:[{}]", param.asJson());
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("格式 转换 异常：[{" + e.getMessage() + "}]");
            exception.setUserMessage("格式 转换 异常");
            exception.setUserTitle("格式 转换 异常");
            throw exception;
        }

        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("param is empty");
            throw exception;
        }

//        try {
//            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"PUT");
//        } catch (Exception  e) {
//            JbiComponentException exception = new JbiComponentException(2000, 1000);
//            exception.setUserTitle("User rights validation failed");
//            exception.setUserMessage(e.getMessage());
//            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
//            throw exception;
//        }

        if (StringUtils.isEmpty(param.getHostname())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("hostname  is empty");
            exception.setUserMessage("hostname is empty");
            exception.setUserTitle("hostname is empty");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getHostip())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("hostip  is empty");
            exception.setUserMessage("hostip is empty");
            exception.setUserTitle("hostip is empty");
            throw exception;
        }

        if (StringUtils.isEmpty(String.valueOf(param.getPort()))) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("port  is empty");
            exception.setUserMessage("port is empty");
            exception.setUserTitle("port is empty");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getUsername())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("username  is empty");
            exception.setUserMessage("username is empty");
            exception.setUserTitle("username is empty");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getPassword())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("password  is empty");
            exception.setUserMessage("password is empty");
            exception.setUserTitle("password is empty");
            throw exception;
        }

        try {
            HostTailDto  hostTailDto = hostService.createHost(param);
            if(hostTailDto==null)
                  hostTailDto=new HostTailDto();
                return   hostTailDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Host Create fail：[{" + e.getMessage() + "}]");
            exception.setUserMessage("Host Create fail");
            exception.setUserTitle("Host Create fail");
            throw exception;
        }
    }
}
