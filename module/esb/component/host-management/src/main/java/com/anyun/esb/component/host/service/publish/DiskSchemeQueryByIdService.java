package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.DiskSchemeService;
import com.anyun.esb.component.host.service.docker.impl.DiskSchemeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

public class DiskSchemeQueryByIdService extends AbstractBusinessService {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DiskSchemeQueryByIdService.class);
    public DiskSchemeService diskSchemeService =new DiskSchemeServiceImpl();


    @Override
    public String getName() {
        return "diskScheme_query_by_id";
    }

    @Override
    public String getDescription() {
        return "DiskScheme Query By Id";
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



        String id= exchange.getIn().getHeader("id",String.class);
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("DiskScheme Id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("DiskScheme Id is empty");
            exception.setUserMessage("DiskScheme Id is  empty");
            exception.setUserTitle("DiskScheme param error");
            throw exception;
        }


        try {
            DiskSchemeDto diskSchemeDto = diskSchemeService.queryDiskSchemeByid(id);
            if (diskSchemeDto == null)
                return new DiskSchemeDto();
            return diskSchemeDto;
        }catch (Exception e){
            LOGGER.debug(e.getMessage());
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("throw new JbiComponentException(2000, 1000);");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("查询磁盘方案详情失败");
            throw exception;
        }
    }
}
