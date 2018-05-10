package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.dto.ContainerVolumeDto;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.storage.common.UUIDVerify;
import com.anyun.esb.component.storage.service.storage.VolumeService;
import com.anyun.esb.component.storage.service.storage.impl.VolumeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 6/22/17.
 */
public class ContainerMountVolumeInfoQueryService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerMountVolumeInfoQueryService.class);
    private VolumeService volumeService;

    public ContainerMountVolumeInfoQueryService() {
        volumeService = new VolumeServiceImpl();
    }

    @Override
    public String getName() {
        return "container_mount_volume_info_query";
    }

    @Override
    public String getDescription() {
        return "ContainerMountVolumeInfoQueryService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "GET");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String container = exchange.getIn().getHeader("container", String.class);
        String volume = exchange.getIn().getHeader("volume", String.class);

        try {
            List<ContainerVolumeDto> l = volumeService.getContainerVolumeDtoList(container, volume);
            if (l == null)
                l = new ArrayList<>();
            return l;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("ContainerMountVolumeInfoQuery  error [" + e.getMessage() + "]");
            exception.setUserMessage("ContainerMountVolumeInfoQuery error[" + e.getMessage() + "]");
            exception.setUserTitle("ContainerMountVolumeInfoQuery error");
            throw exception;
        }
    }
}
