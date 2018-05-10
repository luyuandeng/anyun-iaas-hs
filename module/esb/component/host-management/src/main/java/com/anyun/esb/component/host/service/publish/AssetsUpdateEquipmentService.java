package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AssetsEquipmentDto;
import com.anyun.cloud.param.AssetsUpdateEquipmentParam;
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
public class AssetsUpdateEquipmentService extends AbstractBusinessService {
    private final static Logger LOGGER = LoggerFactory.getLogger(AssetsUpdateEquipmentService.class);
    private AssetsService assetsService;
    private AssetsDao assetsDao;

    public AssetsUpdateEquipmentService(){
        assetsService = new AssetsServiceImpl();
        assetsDao = new AssetsDaoImpl();
    }

    @Override
    public String getName() {
        return "assets_equipment_update";
    }

    @Override
    public String getDescription() {
        return "assets update equipment";
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
        AssetsUpdateEquipmentParam param = JsonUtil.fromJson(AssetsUpdateEquipmentParam.class,postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        //验证资产设备是否存在
        AssetsEquipmentDto dto = assetsService.queryEquipmentDetails(param.getSerialNumber());

        if(dto == null){
            LOGGER.debug("AssetsEquipmentDto is null");
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("AssetsEquipmentDto is null");
            exception.setUserMessage("AssetsEquipmentDto is null");
            exception.setUserTitle("资产管理不存在");
            throw exception;
        }

        try {
           AssetsEquipmentDto assetsEquipmentDto = assetsService.updateEquipment(param);
            return assetsEquipmentDto;
        }catch (Exception e){
            LOGGER.debug("Exception:[{}]",e);
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("AssetsEquipmentDto update error [" + e + "]");
            exception.setUserMessage("区域创建失败[" + e + "]");
            exception.setUserTitle("区域创建失败");
            throw exception;
        }
    }
}
