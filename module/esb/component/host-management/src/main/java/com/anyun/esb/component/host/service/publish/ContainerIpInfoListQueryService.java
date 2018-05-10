package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.NetService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.esb.component.host.service.docker.impl.NetServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jt-workspace on 17-5-11.
 */
public class ContainerIpInfoListQueryService  extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerIpInfoListQueryService.class);
    private NetService netService;

    public ContainerIpInfoListQueryService(){netService=new NetServiceImpl();}

    @Override
    public String getName() {
        return "container_ip_info_list_query";
    }

    @Override
    public String getDescription() {
        return "container_ip_info_list_query";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String s = exchange.getIn().getHeader("s",String.class);
        LOGGER.debug("s:[{}]",s);
        CommonQueryParam commonQueryParam = JsonUtil.fromJson(CommonQueryParam.class, s);
        try {
            UUIDVerify.userRightsVerification(commonQueryParam.getUserUniqueId(),"QUERY");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        try {
            return netService.containeriplistquery(commonQueryParam);
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("查询失败" + e.getMessage());
            exception.setUserMessage("容器ip查询失败" + e.getMessage());
            exception.setUserTitle("容器ip查询失败");
            throw exception;
        }
    }
}
