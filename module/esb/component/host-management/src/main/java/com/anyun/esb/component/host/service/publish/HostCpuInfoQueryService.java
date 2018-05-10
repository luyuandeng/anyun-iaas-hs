package com.anyun.esb.component.host.service.publish;
import com.anyun.cloud.dto.HostCpuInfoDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Created by sxt on 16-7-7.
 */
public class HostCpuInfoQueryService extends AbstractBusinessService{
    private static final Logger LOGGER = LoggerFactory.getLogger(HostApprovalService.class);
    private HostBaseInfoDao hostBaseInfoDao;

    public HostCpuInfoQueryService(){
        hostBaseInfoDao = new HostBaseInfoDaoImpl();
    }

    @Override
    public String getName() {
        return "hostCpuInfo_query";
    }

    @Override
    public String getDescription() {
        return "HostCpuInfoQueryService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);

        try {
            UUIDVerify.userRightsVerification(userUniqueId,"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        HostCpuInfoDto   hostCpuInfoDto=new HostCpuInfoDto();
        hostCpuInfoDto.setCpuFamily(hostBaseInfoDao.selectHostAllCpuFamily());
        hostCpuInfoDto.setCpuCoreLimit(hostBaseInfoDao.selectHostAllCpuCoreLimit());
        return hostCpuInfoDto;
    }
}
