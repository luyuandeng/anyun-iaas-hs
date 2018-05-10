package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.storage.common.UUIDVerify;
import com.anyun.esb.component.storage.service.storage.StorageService;
import com.anyun.esb.component.storage.service.storage.VolumeService;
import com.anyun.esb.component.storage.service.storage.impl.StorageServiceImpl;
import com.anyun.esb.component.storage.service.storage.impl.VolumeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 17-3-7.
 */
public class VolumeQueryService  extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(VolumeQueryService.class);
    private VolumeService volumeService;


    public VolumeQueryService() {
        volumeService = new VolumeServiceImpl();
    }

    @Override
    public String getName() {
        return "volume_query";
    }

    @Override
    public String getDescription() {
        return "VolumeQueryService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "GET");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String subMethod = exchange.getIn().getHeader("subMethod", String.class);
        LOGGER.debug("subMethod:[{}]", subMethod);
        if (StringUtils.isEmpty(subMethod)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("subMethod is empty");
            exception.setUserMessage("subMethod is  empty");
            exception.setUserTitle("subMethod param error");
            throw exception;
        }

        String subParameters = exchange.getIn().getHeader("subParameters",String.class);
        LOGGER.debug("subParameters:[{}]",subParameters);
        if(StringUtils.isEmpty(subParameters)){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("subParameters is empty");
            exception.setUserMessage("subParameters is  empty");
            exception.setUserTitle("subParameters param error");
            throw exception;
        }
        try {
            List<VolumeDto> l = volumeService.VolumeQuery(userUniqueId, subMethod,subParameters);
            if (l == null || l.isEmpty())
                return new ArrayList<VolumeDto>();
            return l;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Volume query fail: ["+e.getMessage()+"]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("查询卷 失败");
            throw exception;
        }
    }
}
