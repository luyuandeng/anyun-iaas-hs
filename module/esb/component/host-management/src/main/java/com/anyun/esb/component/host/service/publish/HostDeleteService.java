package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.HostTailDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.HostService;
import com.anyun.esb.component.host.service.docker.impl.ContainerServiceImpl;
import com.anyun.esb.component.host.service.docker.impl.HostServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by zy on 17-4-17.
 */
public class HostDeleteService extends AbstractBusinessService{
    private final static Logger LOGGER = LoggerFactory.getLogger(HostDeleteService.class);
    private HostService hostService;
    private ContainerService containerService;

    public HostDeleteService(){
        hostService=new HostServiceImpl();
        containerService=new ContainerServiceImpl();
    }

    @Override
    public String getName() {
        return "host_delete";
    }

    @Override
    public String getDescription() {
        return "host delete service";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId= exchange.getIn().getHeader("userUniqueId",String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"DELETE");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        String ip=exchange.getIn().getHeader("ip",String.class);
        LOGGER.debug("ip:[{}]", ip);
        if (StringUtils.isEmpty(ip)) {
            LOGGER.debug("Host Ip is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Host Ip is empty");
            exception.setUserMessage("Host Ip is  empty");
            exception.setUserTitle("Host param error");
            throw exception;
        }
        try{
            //验证是否存在
            HostTailDto dto = hostService.selectHostInfoByDescription(ip);
            if (dto == null){
                  return new Status<String>("success");
            }
            List<String> list=containerService.selectContianerByHostId(dto.getId());
            Iterator<String>  iterator=list.iterator();
            while(iterator.hasNext()){
                String  id=iterator.next();
                try {
                    if("".equals(id))
                        continue;
                    containerService.operationContainer(id,"delete");
                }catch (Exception e){
                    LOGGER.debug("container:[{}] delete fail :[{}]",id,e.getMessage());
                }
            }
            hostService.deleteHost(dto.getId(),ip);
            return new Status<String>("success");
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("host  error [" + e + "]");
            exception.setUserMessage("宿主机删除失败[" + e + "]");
            exception.setUserTitle("宿主机删除失败");
            throw exception;
        }
    }
}
