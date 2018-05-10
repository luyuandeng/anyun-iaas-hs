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

/**
 * Created by sxt on 16-8-11.
 */
public class VolumeQueryByIdService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VolumeQueryByIdService.class);
    private VolumeService volumeService;

    public VolumeQueryByIdService() {
        volumeService = new VolumeServiceImpl();
    }

    @Override
    public String getName() {
        return "volume_query_by_id";
    }

    @Override
    public String getDescription() {
        return "VolumeQueryByIdService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String id = exchange.getIn().getHeader("id", String.class);
        if (StringUtils.isEmpty(id)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param [id] is empty");
            exception.setUserMessage("服务参数格式化错误");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        try {
            VolumeDto volumeDto = volumeService.volumeQueryById(id);
            if (volumeDto == null)
                return new VolumeDto();
            return volumeDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("volume query error [" + e.getMessage() + "]");
            exception.setUserMessage("卷 查询失败[" + e.getMessage() + "]");
            exception.setUserTitle("卷查询失败");
            throw exception;
        }
    }
}
