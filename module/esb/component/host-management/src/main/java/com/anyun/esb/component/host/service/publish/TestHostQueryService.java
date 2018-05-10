package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.DockerImageDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gaopeng on 16-6-2.
 */
public class TestHostQueryService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestHostQueryService.class);

    @Override
    public String getName() {
        return "host_query_test";
    }

    @Override
    public String getDescription() {
        return "宿主机查询测试";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
//        String statusValue = exchange.getIn().getHeader("status", String.class);
//        LOGGER.debug("测试的机器状态[{}]",statusValue);
//        String postBody = exchange.getIn().getBody(String.class);
////        LOGGER.debug("上传的文档[{}]",postBody);
//        List<HostBaseInfoDto> hosts = new ArrayList<>();
//        HostBaseInfoDto  dto = new HostBaseInfoDto();
//        dto.setCreateDate(new Date());
//        dto.setDescript("测试的虚拟机");
//        DockerHostInfoDto infoDto = new DockerHostInfoDto();
//        infoDto.setCreateDate(new Date());
//        infoDto.setArch("x86");
//        infoDto.setVersion("1.11.0");
//        dto.setDockerHostInfoDto(infoDto);
//        HostManagementIpInfoDto ip = new HostManagementIpInfoDto();
//        ip.setCreateDate(new Date());
//        ip.setIpAddress("172.20.0.1");
//        dto.setHostManagementIpInfoDto(ip);
//        hosts.add(dto);
//        return hosts;

        ServiceInvoker<PageDto<DockerImageDto>> invoker = new ServiceInvoker<>();
        invoker.setCamelContext(endpoint.getCamelContext());
        invoker.setComponent("anyun-registry");
        invoker.setService("template_search");
        try {
            invoker.invoke(null,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.debug("******************************");
        return new Status<String>("SUCCESS");
    }
}
