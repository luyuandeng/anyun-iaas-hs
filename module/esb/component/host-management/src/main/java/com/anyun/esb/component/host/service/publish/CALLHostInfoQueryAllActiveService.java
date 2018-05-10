package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.DockerHostService;
import com.anyun.esb.component.host.service.docker.impl.DockerHostServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-8-16.
 */
public class CALLHostInfoQueryAllActiveService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CALLHostInfoQueryAllActiveService.class);
    private DockerHostService dockerHostService;

    public CALLHostInfoQueryAllActiveService() {
        dockerHostService = new DockerHostServiceImpl();
    }

    @Override
    public String getName() {
        return "hostInfo_query_all_active";
    }

    @Override
    public String getDescription() {
        return "HostInfoQueryAllActiveService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        List<String> hostIp = new ArrayList<String>();
        try {
            hostIp = dockerHostService.findAllActiveHosts();
            if (hostIp.isEmpty()) {
                LOGGER.debug("hostIp is empty");
                JbiComponentException exception = new JbiComponentException(2000, 1000);
                exception.setUserTitle("hostIp is empty");
                exception.setUserMessage("hostIp is empty");
                exception.setMessage("hostIp is empty");
                throw exception;
            }
            LOGGER.debug("hostIp {}", hostIp);
            String str = "";
            for (String s : hostIp) {
                str = str + s + ",";
            }
            if (StringUtils.isNotEmpty(str)) {
                str = str.substring(0, str.length() - 1).trim();
            }
            LOGGER.debug("str:{}", str);
            return str;
        } catch (Exception e) {
            LOGGER.debug("Exception is:[{}]", e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("find All Active Hosts error");
            exception.setUserMessage("find All Active Hosts error");
            exception.setMessage(e.getMessage());
            throw exception;
        }
    }
}
