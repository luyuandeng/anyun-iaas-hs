package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.AssetsService;
import com.anyun.esb.component.host.service.docker.impl.AssetsServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

public class AssetsDeleteByIdService extends AbstractBusinessService {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AssetsDeleteByIdService.class);
    private AssetsService assetsService;

    public AssetsDeleteByIdService(){
        assetsService = new AssetsServiceImpl();
    }




    @Override
    public String getName() {
        return "assets_delete_by_id";
    }

    @Override
    public String getDescription() {
        return "Assets Delete By Id";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        try {
            UUIDVerify.userRightsVerification(userUniqueId, "DELETE");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }
        String id = exchange.getIn().getHeader("id", String.class);
        if (StringUtils.isEmpty(id)) {
            LOGGER.debug("is Id is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Id is empty");
            exception.setUserMessage("Id is  empty");
            exception.setUserTitle("Id is  empty");
            throw exception;
        }

        try {
            AssetsDto dto = assetsService.QueryAssetsInfo(id);
            if(dto!=null){
                assetsService.deleteAsserts(id);
            }
            return new Status<String>("success");
        } catch (Exception e) {
            LOGGER.debug("Exception:[{}]", e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("assets delete error [" + e + "]");
            exception.setUserMessage("Assets删除失败[" + e + "]");
            exception.setUserTitle("assets失败");
            throw exception;
        }
    }
}
