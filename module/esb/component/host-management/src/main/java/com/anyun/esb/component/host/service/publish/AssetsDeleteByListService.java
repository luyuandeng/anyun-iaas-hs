package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.AssetsDto;
import com.anyun.cloud.param.AssetsCreateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.service.docker.AssetsService;
import com.anyun.esb.component.host.service.docker.impl.AssetsServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by jt on 17-11-9.
 */
public class AssetsDeleteByListService extends AbstractBusinessService {

    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AssetsDeleteByListService.class);
    private AssetsService assetsService;

    public AssetsDeleteByListService() {
        assetsService = new AssetsServiceImpl();
    }


    @Override
    public String getName() {
        return "assets_delete_by_list";
    }

    @Override
    public String getDescription() {
        return "Assets Delete By List";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String userUniqueId = exchange.getIn().getHeader("userUniqueId", String.class);
        String ids = exchange.getIn().getHeader("id", String.class);
        if (StringUtils.isEmpty(ids))
            return new Status<String>("id is null");

        List<String> list = Arrays.asList(ids.split(","));
        try {
            for (String str : list) {
                if (StringUtils.isEmpty(str))
                    continue;
                AssetsDto dto = assetsService.QueryAssetsInfo(str);
                if (dto == null)
                    continue;
                assetsService.deleteAsserts(str);
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
