package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.InterfaceConfigDto;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.InterfaceService;
import com.anyun.esb.component.host.service.docker.impl.InterfaceServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;

/**
 * Created by gp on 17-1-3.
 */
public class InterfaceConfigService extends AbstractBusinessService {
    private InterfaceService interfaceService;

    public InterfaceConfigService(){
        interfaceService=new InterfaceServiceImpl();
    }

    @Override
    public String getName() {
        return "query_interface_config";
    }

    @Override
    public String getDescription() {
        return "Query Interface Config By Name";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String name = exchange.getIn().getHeader("name",String.class);
        try {
            InterfaceConfigDto dto =interfaceService.queryInterfaceConfig(name);
            if (dto == null){
                return new InterfaceConfigDto();
            }
            return dto;
        }catch (Exception e){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("接口配置信息查詢失敗["+e.getMessage()+"]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("接口配置信息查詢失敗");
            throw exception;
        }
    }
}
