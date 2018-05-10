package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.RemoveIpFromSecurityGroupParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.SecurityGroupService;
import com.anyun.esb.component.host.service.docker.impl.SecurityGroupServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-10-18.
 */
public class RemoveIpFromSecurityGroupService extends AbstractBusinessService {
    private final static Logger LOGGER  = LoggerFactory.getLogger(RemoveIpFromSecurityGroupService.class);
    private SecurityGroupService securityGroupService;
    public  RemoveIpFromSecurityGroupService(){
        securityGroupService=new SecurityGroupServiceImpl();
    }

    @Override
    public String getName() {
        return "remove_ip_from_securityGroup";
    }

    @Override
    public String getDescription() {
        return "RemoveIpFromSecurityGroupService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody=exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]",postBody);
        if(StringUtils.isEmpty(postBody)){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("postBody is empty");
            throw exception;
        }

        RemoveIpFromSecurityGroupParam param= JsonUtil.fromJson(RemoveIpFromSecurityGroupParam.class,postBody);
        LOGGER.debug("param:[{}]",postBody);
        if(param==null){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("param is empty");
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


        try{
            securityGroupService.removeIpFromSecurityGroup(param);
            return  new Status<String>("success");
        }catch(Exception  e){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("removeIpFromSecurityGroup fail [" + e.getMessage() + "]");
            exception.setUserMessage("removeIpFromSecurityGroup fail： [" + e.getMessage() + "]");
            exception.setUserTitle("removeIpFromSecurityGroup fail");
            throw exception;
        }
    }
}
