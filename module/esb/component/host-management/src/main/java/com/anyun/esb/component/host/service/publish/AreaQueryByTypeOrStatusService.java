package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.AreaDto;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.AreaDao;
import com.anyun.esb.component.host.dao.impl.AreaDaoImpl;
import com.anyun.esb.component.host.service.docker.AreaService;
import com.anyun.esb.component.host.service.docker.impl.AreaServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by twh-workspace on 17-3-23.
 */
public class AreaQueryByTypeOrStatusService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AreaQueryByTypeOrStatusService.class);
    private AreaService areaService;
    private AreaDao areaDao;

    public AreaQueryByTypeOrStatusService() {
        areaService = new AreaServiceImpl();
        areaDao =new AreaDaoImpl();
    }

    @Override
    public String getName() {
        return "area_query_by_typeOrStatus";
    }

    @Override
    public String getDescription() {
        return "AreaQueryByTypeOrStatusService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        LOGGER.debug("userUniqueId:[{}]", userUniqueId);
        try {
            UUIDVerify.userRightsVerification(userUniqueId,"GET");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        String type = exchange.getIn().getHeader("type",String.class);
        String status = exchange.getIn().getHeader("status",String.class);
        List<AreaDto> l = null;

        if ((type.equals("") || type ==null )&& (status.equals("") || status ==null)){
            l = areaDao.queryAllArea();
        }

        if((type.equals("") || type ==null ) && ( !status.equals("") || status != null)){
            l = areaService.queryAeaByStatus(status);
        }


        if((!type.equals("") || type !=null ) && ( status.equals("") || status == null)){
            l = areaService.queryAeaBytype(type);
        }

        if((!type.equals("") || type !=null ) && ( !status.equals("") || status != null)){
           l = areaService.queryAeaBytypeAndStatus(type,status);
        }
        try {
            if (l == null)
                return new ArrayList<AreaDto>();
            return l;
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("AreaQueryByTypeOrStatusService fail[" + e.getMessage() + "]");
            exception.setUserMessage(e.getMessage());
            exception.setUserTitle("AreaQueryByTypeOrStatus fail");
            throw exception;
        }
    }
}
