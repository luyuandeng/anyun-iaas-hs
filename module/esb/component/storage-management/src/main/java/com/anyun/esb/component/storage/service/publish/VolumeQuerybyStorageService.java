package com.anyun.esb.component.storage.service.publish;

import com.anyun.cloud.dto.VolumeDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
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
 * Created by sxt on 8/14/17.
 */
public class VolumeQuerybyStorageService extends AbstractBusinessService {
    private final static  Logger LOGGER = LoggerFactory.getLogger(VolumeQuerybyStorageService.class);
    private VolumeService volumeService;
    public VolumeQuerybyStorageService(){
        volumeService =new VolumeServiceImpl();
    }

    @Override
    public String getName() {
        return "volume_query_by_storage";
    }

    @Override
    public String getDescription() {
        return "VolumeQuerybyStorageService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {

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
            List<VolumeDto> volumeDtoList = volumeService.volumeQueryByStorageId(id);
            if (volumeDtoList == null)
                return new ArrayList<VolumeDto>();
            return volumeDtoList;
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("Exception is{}", e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("volume query error [" + e.getMessage() + "]");
            exception.setUserMessage("卷 查询失败[" + e.getMessage() + "]");
            exception.setUserTitle("卷查询失败");
            throw exception;
        }
    }
}
