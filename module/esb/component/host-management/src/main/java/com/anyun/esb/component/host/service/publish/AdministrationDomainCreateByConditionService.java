package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.param.DomainCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.AdministrationDomainService;
import com.anyun.esb.component.host.service.docker.impl.AdministrationDomainServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by twh-workspace on 17-2-22.
 */
public class AdministrationDomainCreateByConditionService extends AbstractBusinessService{
    private static final Logger LOGGER = LoggerFactory.getLogger(AdministrationDomainCreateByConditionService.class);
    private AdministrationDomainService administrationDomainService;

    public AdministrationDomainCreateByConditionService() {
        administrationDomainService = new AdministrationDomainServiceImpl();
    }

    @Override
    public String getName() {
        return "AdministrationDomain_create_by_condition";
    }

    @Override
    public String getDescription() {
        return "AdministrationDomainCreateByConditionService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {

        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);

        if(StringUtils.isEmpty(postBody)){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        DomainCreateParam param = JsonUtil.fromJson(DomainCreateParam.class,postBody);
        LOGGER.debug("param:[{}]",param.asJson());

        if(param == null){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is null");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if(StringUtils.isEmpty(param.getId())){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[Id] is empty");
            exception.setUserMessage("服务参数 [Id]  is  empty");
            exception.setUserTitle("参数 Id 验证失败");
            throw exception;
        }

        if(StringUtils.isEmpty(param.getDomain())){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[Domain] is empty");
            exception.setUserMessage("服务参数 [Domain]  is  empty");
            exception.setUserTitle("参数 Domain 验证失败");
            throw exception;
        }

        if(StringUtils.isEmpty(param.getIp())){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[IP] is empty");
            exception.setUserMessage("服务参数 [IP]  is  empty");
            exception.setUserTitle("参数 IP 验证失败");
            throw exception;
        }

        if(StringUtils.isEmpty(param.getContainerId())){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("service param[ContainerId] is empty");
            exception.setUserMessage("服务参数 [ContainerId]  is  empty");
            exception.setUserTitle("参数 ContainerId 验证失败");
            throw exception;
        }

        try{
            administrationDomainService.createAdministrationDomainByCondition(param);
            return new Status<String>("success");
        }
        catch (Exception ex){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("AdministrationDomain create error [" + ex.getMessage() + "]");
            exception.setUserMessage("管理域创建失败[" + ex.getMessage() + "]");
            exception.setUserTitle("管理域创建失败");
            throw exception;
        }
    }
}
