package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.HostMemoryInfoDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by gaopeng on 16-7-5.
 */
public class HostMemoryInfoQueryService extends AbstractBusinessService {
    private HostBaseInfoDao  hostBaseInfoDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(HostMemoryInfoQueryService.class);
    public HostMemoryInfoQueryService(){
        hostBaseInfoDao =new HostBaseInfoDaoImpl();
    }
    @Override
    public String getName(){return "hostMemoryInfo_query";}

    @Override
    public String getDescription() {
        return "HostMemoryInfoQueryService";
    }

    @Override
    public Object process(Endpoint endpoint,Exchange exchange) throws JbiComponentException{
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

        try{
            List<HostMemoryInfoDto>   dtos=hostBaseInfoDao.selectHostMemoryInfo();
            if(dtos==null)
                return  new ArrayList<>();
            return dtos;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("HostMemoryInfo  Query  error[" + e.getMessage() + "]");
            exception.setUserMessage("HostMemoryInfo  Query  error [" + e.getMessage() + "]");
            exception.setUserTitle("HostMemoryInfo  Query  error");
            throw exception;
        }
    }
}
