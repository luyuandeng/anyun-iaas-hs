package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.DiskSchemeService;
import com.anyun.esb.component.host.service.docker.impl.DiskSchemeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DiskSchemeListQueryService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(DiskSchemeListQueryService.class);
    private DiskSchemeService diskSchemeService;
    public DiskSchemeListQueryService(){
        diskSchemeService =new DiskSchemeServiceImpl();
    }



    @Override
    public String getName() {
        return "diskScheme_list_query";
    }

    @Override
    public String getDescription() {
        return "diskScheme List Query";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {

        String userUniqueId= exchange.getIn().getHeader("userUniqueId",String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        try {
            List<DiskSchemeDto> diskSchemeDtos = diskSchemeService.queryDiskSchemeList(userUniqueId);
            if (diskSchemeDtos == null)
                return new ArrayList<DiskSchemeDto>();
            return diskSchemeDtos;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("查询磁盘方案列表失败");
            throw exception;
        }
    }
}
