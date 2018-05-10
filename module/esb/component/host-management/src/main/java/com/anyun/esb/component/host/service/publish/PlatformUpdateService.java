package com.anyun.esb.component.host.service.publish;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.PlatformDto;
import com.anyun.cloud.param.PlatformUpdateParam;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.AbstractBusinessService;
import com.anyun.esb.component.host.common.UUIDVerify;
import com.anyun.esb.component.host.dao.PlatformDao;
import com.anyun.esb.component.host.dao.impl.PlatformDaoImpl;
import com.anyun.esb.component.host.service.docker.PlatformService;
import com.anyun.esb.component.host.service.docker.impl.PlatformServiceImpl;
import com.anyun.exception.JbiComponentException;
import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 16-10-13.
 */
public class PlatformUpdateService extends AbstractBusinessService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlatformUpdateService.class);
    private PlatformService platformService;
    private PlatformDao platformDao;

    public PlatformUpdateService() {
        platformService = new PlatformServiceImpl();
        platformDao = new PlatformDaoImpl();
    }

    @Override
    public String getName() {
        return "platform_update";
    }

    @Override
    public String getDescription() {
        return "PlatformUpdateService";
    }

    @Override
    public Object process(Endpoint endpoint, Exchange exchange) throws JbiComponentException {
        String postBody = exchange.getIn().getBody(String.class);
        LOGGER.debug("postBody:[{}]", postBody);
        if (StringUtils.isEmpty(postBody)) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("postBody is empty");
            exception.setUserMessage("postBody is empty");
            exception.setUserTitle("param check error");
            throw exception;
        }
        PlatformUpdateParam param = JsonUtil.fromJson(PlatformUpdateParam.class, postBody);
        LOGGER.debug("param:[{}]", param.asJson());
        if (param == null) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("param is null");
            exception.setUserMessage("param is null");
            exception.setUserTitle("param check error");
            throw exception;
        }

        try {
            UUIDVerify.userRightsVerification(param.getUserUniqueId(),"POST");
        } catch (Exception  e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("User rights validation failed");
            exception.setUserMessage(e.getMessage());
            exception.setMessage("User rights validation failed:{" + e.getMessage() + "}");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("id is empty");
            exception.setUserMessage("is is empty");
            exception.setMessage("is is empty");
            throw exception;
        }

        PlatformDto platformDto = platformDao.selectPlatformById(param.getId());
        if (platformDto == null || StringUtils.isEmpty(platformDto.getId())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("The param  id  does not  exists");
            exception.setUserMessage("The param  id  does not  exists");
            exception.setMessage("The param  id  does not  exists");
            throw exception;
        }

        if (StringUtils.isEmpty(param.getIpDomain())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("ipDomain is empty");
            exception.setUserMessage("param is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        //IP 合法校驗  域名合法校驗
        String regexip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."+"(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        String regexdomain = "[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";//域名的正則表達式


        if(!param.getIpDomain().matches(regexip) && !param.getIpDomain().matches(regexdomain)){
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("ipDomain is error");
            exception.setUserMessage("param is error");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }




        if (StringUtils.isEmpty(param.getPort())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("port is empty");
            exception.setUserMessage("port is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }
        if (StringUtils.isEmpty(param.getBaseUrl())) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("baseUrl is empty");
            exception.setUserMessage("baseUrl is empty");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        if (param.getStatus() != 0 && param.getStatus() != 1) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setMessage("The param status is  not  in (0,1)");
            exception.setUserMessage("The param status is  not  in (0,1)");
            exception.setUserTitle("参数验证失败");
            throw exception;
        }

        try {
            platformService.platformUpdate(param);
            return new Status<String>("success");
        } catch (Exception e) {
            JbiComponentException exception = new JbiComponentException(2000, 1000);
            exception.setUserTitle("Platform update fail");
            exception.setUserMessage("Platform update fail:" + e.getMessage());
            exception.setMessage("Platform update fail:{" + e.getMessage() + "}");
            throw exception;
        }
    }
}
