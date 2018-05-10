package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.DiskSchemeDto;
import com.anyun.cloud.dto.PageDto;
import com.anyun.cloud.param.CommonQueryParam;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.DiskSchemeService;
import com.anyun.esb.component.host.service.docker.impl.DiskSchemeServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

public class DiskSchemeQueryByConditionsService  extends AbstractBusinessService {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(DiskSchemeQueryByConditionsService.class);
    private DiskSchemeService diskSchemeService;

    public DiskSchemeQueryByConditionsService(){
        diskSchemeService = new DiskSchemeServiceImpl();
    }


    @Override
    public String getName() {
        return "diskScheme_query_by_conditions";
    }

    @Override
    public String getDescription() {
        return "diskScheme Query By Conditions";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String s = exchange.getIn().getHeader("s",String.class);
        LOGGER.debug("s:[{}]",s);
        CommonQueryParam commonQueryParam = JsonUtil.fromJson(CommonQueryParam.class, s);
        try {
            UUIDVerify.userRightsVerification(commonQueryParam.getUserUniqueId(),"QUERY");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        try {
            PageDto<DiskSchemeDto> diskSchemeDtoPageDto = diskSchemeService.queryDiskSchemeDtoBycondition(commonQueryParam);
            if(diskSchemeDtoPageDto==null)
                return new  PageDto<DiskSchemeDto>();
            return diskSchemeDtoPageDto;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("磁盘方案查询失败" + e.getMessage());
            exception.setUserMessage("磁盘方案查询失败" + e.getMessage());
            exception.setUserTitle("磁盘方案查询失败");
            throw exception;
        }
    }
}
