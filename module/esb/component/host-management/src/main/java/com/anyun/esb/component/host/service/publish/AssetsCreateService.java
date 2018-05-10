package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.param.AssetsCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.service.docker.AssetsService;
import com.anyun.esb.component.host.service.docker.impl.AssetsServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssetsCreateService extends AbstractBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AssetsCreateService.class);

    public AssetsCreateService() {
        assetsService = new AssetsServiceImpl();
    }

    private AssetsService assetsService;

    @Override
    public String getName() {
        return "assets_base_create";
    }

    @Override
    public String getDescription() {
        return "assets_base_create service";
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


        List<Map> list = JsonUtil.fromJson(List.class, postBody);
        List<AssetsCreateParam> params = new ArrayList<>();
        for (Map map : list) {
            if (map.isEmpty())
                continue;
            AssetsCreateParam a = JsonUtil.fromJson(AssetsCreateParam.class, JsonUtil.toJson(map));
            if (!a.getDeviceCategory().matches("SERVER|ROUTER|SWITCH|OTHER"))
                continue;
            params.add(a);
        }

        if (params == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("Service param format error");
            exception.setUserMessage("服务参数格式化错误[" + exception + "]");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (params.size() == 0) {
            new ArrayList<>();
        }

        try {
            List<AssetsDto> l = assetsService.createAssets(params);
            if (l == null)
                l = new ArrayList<>();
            return l;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
