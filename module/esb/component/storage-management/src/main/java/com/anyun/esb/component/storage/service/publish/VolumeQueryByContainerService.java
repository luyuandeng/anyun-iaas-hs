package com.anyun.esb.component.storage.service.publish;

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
 * Created by sxt on 16-8-12.
 */
public class VolumeQueryByContainerService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VolumeQueryByContainerService.class);
    private VolumeService volumeService;

    public VolumeQueryByContainerService() {
        volumeService = new VolumeServiceImpl();
    }

    @Override
    public String getName() {
        return "volume_query_by_container";
    }

    @Override
    public String getDescription() {
        return "VolumeQueryByContainerService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
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

        String id = exchange.getIn().getHeader("id", String.class);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("param [id] is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [id] is empty");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            LOGGER.debug("[id] is:{}", id);
            List<VolumeDto> volumeDtoList = volumeService.volumeQueryByContainer(id);
            if (volumeDtoList == null)
                return new ArrayList<VolumeDto>();
            return volumeDtoList;
        } catch (Exception e) {
            LOGGER.debug("Exception is{}", e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("volume query error [" + e.getMessage() + "]");
            exception.setUserMessage("卷 查询失败[" + e.getMessage() + "]");
            exception.setUserTitle("卷查询失败");
            throw exception;
        }
    }
}


