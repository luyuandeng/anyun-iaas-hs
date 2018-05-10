package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.dto.AssetsEquipmentDto;
import com.anyun.cloud.param.AssetsCreateEquipmentParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.dao.AssetsDao;
import com.anyun.esb.component.host.dao.impl.AssetsDaoImpl;
import com.anyun.esb.component.host.service.docker.AssetsService;
import com.anyun.esb.component.host.service.docker.impl.AssetsServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jt-workspace on 17-4-13.
 */
public class AssetsCreateEquipmentService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AssetsCreateEquipmentService.class);
    private AssetsService assetsService;
    private AssetsDao assetsDao;

    public AssetsCreateEquipmentService(){
        assetsService = new AssetsServiceImpl();
        assetsDao = new AssetsDaoImpl();
    }

    @Override
    public String getName() {
        return "assets_equipment_create";
    }

    @Override
    public String getDescription() {
        return "assets create equipment";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            LOGGER.debug("postBody is empty");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        //转化参数实体类，验证参数
        AssetsCreateEquipmentParam param = JsonUtil.fromJson(AssetsCreateEquipmentParam.class,postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if(StringUtils.isEmpty(param.getSerialNumber())){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param Equipment format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("序列号验证失败");
            throw exception;
        }

        if(param.getMaintenancePeriod() == null){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param Equipment format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("时间验证失败");
            throw exception;
        }


        try {

            //验证序列号
            AssetsEquipmentDto assetsEquipmentDto = assetsService.queryEquipmentDetails(param.getSerialNumber());
            if(assetsEquipmentDto!=null)
                return  assetsEquipmentDto;
            AssetsEquipmentDto dto = assetsService.createEquipment(param);
            if(dto==null)
                return  new AssetsEquipmentDto();
            return dto;
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Assets  create error [" + e + "]");
            exception.setUserMessage("创建失败[" + e + "]");
            exception.setUserTitle("创建失败");
            throw exception;
        }
    }
}
