package com.anyun.esb.component.registry.service.publish;

import com.anyun.cloud.dto.PictureDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.registry.common.UUIDVerify;
import com.anyun.esb.component.registry.service.core.PictureService;
import com.anyun.esb.component.registry.service.core.impl.PictureServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sxt on 16-10-18.
 */
public class PictureQueryAllService extends AbstractBusinessService{
    private static final Logger LOGGER = LoggerFactory.getLogger(PictureQueryAllService.class);
    private PictureService pictureService;

    public PictureQueryAllService() {
        pictureService = new PictureServiceImpl();
    }

    @Override
    public String getName() {
        return "picture_query_all";
    }

    @Override
    public String getDescription() {
        return "PictureQueryAllService";
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
        try {
            List<PictureDto>  list = pictureService.pictureQueryAll();
            if(list==null)
                return  new ArrayList<>();
            return   list;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("PictureQueryAll fail");
            exception.setUserMessage("PictureQueryAll fail[" + exception + "]");
            exception.setUserTitle("PictureQueryAll fail");
            throw exception;
        }
    }
}
