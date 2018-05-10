package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.AdministrationDomainService;
import com.anyun.esb.component.host.service.docker.impl.AdministrationDomainServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by twh-workspace on 17-2-23.
 */
public class AdministrationDomainDeleteByIdService extends AbstractBusinessService{

    private static final Logger LOGGER = LoggerFactory.getLogger(AdministrationDomainDeleteByIdService.class);
    private AdministrationDomainService administrationDomainService;

    public AdministrationDomainDeleteByIdService() {
        administrationDomainService = new AdministrationDomainServiceImpl();
    }

    @Override
    public String getName() {
        return "AdministrationDomainDeleteByIdService";
    }

    @Override
    public String getDescription() {
        return "AdministrationDomain_delete_by_id";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String id = exchange.getIn().getHeader("id",String.class);
        LOGGER.debug("id:[{id}]",id);
        if(StringUtils.isEmpty(id)){
            LOGGER.debug("id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try{
            administrationDomainService.administrationDomainDeleteById(id);
            return new Status<String>("success");
        }
        catch(Exception ex){
            LOGGER.debug(ex.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("administrationDomain  delete  error  info[" + ex.getMessage() + "]");
            exception.setUserMessage("管理域删除错误信息 [" + ex.getMessage() + "]");
            exception.setUserTitle("管理域删除错误");
            throw exception;
        }
    }
}
