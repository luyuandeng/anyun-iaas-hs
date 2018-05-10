package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.param.DiskSchemeCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.DiskSchemeService;
import com.anyun.esb.component.host.service.docker.impl.DiskSchemeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiskSchemeCreateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiskSchemeCreateService.class);
    private DiskSchemeService diskSchemeService;

    public DiskSchemeCreateService() {
        diskSchemeService = new DiskSchemeServiceImpl();
    }

    @Override
    public String getName() {
        return "diskScheme_create";
    }

    @Override
    public String getDescription() {
        return "diskScheme Create";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);


        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }



        DiskSchemeCreateParam diskSchemeCreateParam = JsonUtil.fromJson(DiskSchemeCreateParam.class, postBody);
        LOGGER.debug("param:[{}]", diskSchemeCreateParam.asJson());
        if (diskSchemeCreateParam == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is null");
            exception.setUserMessage("param is null");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        LOGGER.debug(diskSchemeCreateParam.getDescription());

        if(diskSchemeCreateParam.getDescription().isEmpty())
        {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("磁盘方案的描述为空");
            exception.setUserMessage("磁盘方案的描述为空");
            exception.setUserTitle("参数验证失败");
            throw exception;


        }
        if(diskSchemeCreateParam.getName().isEmpty()){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("磁盘方案的名字为空");
            exception.setUserMessage("磁盘方案的名字为空");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        if(diskSchemeCreateParam.getSize()<20){

            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("磁盘大小不满足20");
            exception.setUserMessage("磁盘大小不满足20");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }




        try {
            DiskSchemeDto diskSchemeDto= diskSchemeService.createDiskSchemeDto(diskSchemeCreateParam);
            return diskSchemeDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("创建磁盘方案失败:" + e.getMessage());
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("创建磁盘方案失败");
            throw exception;
        }
    }
}
