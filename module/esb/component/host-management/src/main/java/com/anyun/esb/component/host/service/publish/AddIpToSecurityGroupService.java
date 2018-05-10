package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.AddIpToSecurityGroupParam;
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
public class AddIpToSecurityGroupService  extends AbstractBusinessService{
    private  final  static Logger  LOGGER = LoggerFactory.getLogger(AddIpToSecurityGroupService.class);
    private SecurityGroupService  securityGroupService;
    public AddIpToSecurityGroupService(){
        securityGroupService=new SecurityGroupServiceImpl();
    }

    @Override
    public String getName() {
        return "add_ip_to_securityGroup";
    }

    @Override
    public String getDescription() {
        return "AddIpToSecurityGroupService";
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

        AddIpToSecurityGroupParam  param= JsonUtil.fromJson(AddIpToSecurityGroupParam.class,postBody);
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
            securityGroupService.AddIpToSecurityGroup(param);
            return  new Status<String>("success");
        }catch(Exception  e){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("AddIpToSecurityGroup fail [" + e.getMessage() + "]");
            exception.setUserMessage("AddIpToSecurityGroup fail： [" + e.getMessage() + "]");
            exception.setUserTitle("AddIpToSecurityGroup fail");
            throw exception;
        }
    }
}
