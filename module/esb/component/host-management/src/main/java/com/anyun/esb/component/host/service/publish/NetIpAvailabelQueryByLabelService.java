package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.IpDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.NetService;
import com.anyun.esb.component.host.service.docker.impl.NetServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gp on 16-11-10.
 */
public class NetIpAvailabelQueryByLabelService  extends AbstractBusinessService{
    private static final Logger LOGGER = LoggerFactory.getLogger(NetIpAvailabelQueryByLabelService.class);
    private NetService netService;

    public NetIpAvailabelQueryByLabelService() {
        netService=new NetServiceImpl();
    }

    @Override
    public String getName() {
        return "query_available_ip_by_label";
    }

    @Override
    public String getDescription() {
        return "Query Available ip As Briage By Label";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);

        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String label = exchange.getIn().getHeader("label",String.class);
        if (StringUtils.isEmpty(label)) {
            LOGGER.debug("label is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            List<IpDto> list = netService.queryAvailabelIpByLabel(label);
            if (list==null)
                return  new ArrayList<IpDto>();
            LOGGER.debug("result message:"+list.size());
            return list;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("查询 可用 briage 标签 ip失败");
            throw exception;
        }
    }
}
