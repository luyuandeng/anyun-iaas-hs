package com.anyun.esb.component.registry.common;

import com.anyun.cloud.dto.InterfaceConfigDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.ServiceInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sxt on 16-11-28.
 */
public class UUIDVerify {
    private final static Logger LOGGER = LoggerFactory.getLogger(UUIDVerify.class);
    private final static String NAME = "UUID";  //接口名称
    private final static int ENABLE =1 ;  //接口名称

    /**
     * 用户操作权限验证
     *
     * @param userUniqueId
     * @param action
     * @return
     */
    public static void userRightsVerification(String userUniqueId, String action) throws Exception {
        if(ENABLE!=1){
            return ;
        }
        InterfaceConfigDto interfaceConfigDto = getInterfaceInfo();
        if (interfaceConfigDto == null || StringUtils.isEmpty(interfaceConfigDto.getId()))
            return;

        if (interfaceConfigDto.getEnable() != 1)
            return;

        LOGGER.debug("----------interfaceConfigDto:[{}]", JsonUtil.toJson(interfaceConfigDto));
        if (StringUtils.isEmpty(userUniqueId))
            throw new Exception("userUniqueId is empty");
        if (StringUtils.isEmpty(action))
            throw new Exception("className is empty");
        if (!exist(interfaceConfigDto, userUniqueId))
            throw new Exception("用户不存在");
        if (!whetherToAllowOperation(interfaceConfigDto, userUniqueId, action))
            throw new Exception("用户权限不足");
    }


    /**
     * 获取  请求接口信息
     *
     * @return InterfaceConfigDto
     */
    public static InterfaceConfigDto getInterfaceInfo() throws Exception {
        ServiceInvoker<Map> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("query_interface_config");
        Map<String, String> param = new HashMap<>();
        param.put("name", NAME);
        Map response = invoker.invoke(param, null);
        InterfaceConfigDto interfaceConfigDto = JsonUtil.fromJson(InterfaceConfigDto.class, JsonUtil.toJson(response));
        return interfaceConfigDto;
    }

    /**
     * 用户是否存在
     *
     * @param interfaceConfigDto
     * @param userUniqueId
     * @return boolean
     */
    public static boolean exist(InterfaceConfigDto interfaceConfigDto, String userUniqueId) throws Exception {
        HostWebClient.Configuration configuration = new HostWebClient.Configuration();
        configuration.setBaseUrl(interfaceConfigDto.getBaseUrl());
        configuration.setPlatformAddress(interfaceConfigDto.getIp());
        configuration.setPort(interfaceConfigDto.getPort());
        HostWebClient hostWebClient = HostWebClient.build(configuration);
        Map<String, Object> sendParam = new HashMap<>();
        sendParam.put("userUniqueId", userUniqueId);
        LOGGER.debug("sendParam:[{}]", JsonUtil.toJson(sendParam));
        String json = hostWebClient.get("/userIfExists", sendParam);
        LOGGER.debug("json:{}", json);
        if (StringUtils.isEmpty(json))
            throw new Exception("response json is empty");
        Map<String, Object> response = JsonUtil.fromJson(Map.class, json);
        LOGGER.debug(response.get("msg").toString());
        return Boolean.parseBoolean(response.get("msg").toString());
    }


    /**
     * 用户操作查询
     *
     * @param interfaceConfigDto
     * @param userUniqueId
     * @param action
     * @return boolean
     */
    public static boolean whetherToAllowOperation(InterfaceConfigDto interfaceConfigDto, String userUniqueId, String action) throws Exception {
        HostWebClient.Configuration configuration = new HostWebClient.Configuration();
        configuration.setBaseUrl(interfaceConfigDto.getBaseUrl());
        configuration.setPlatformAddress(interfaceConfigDto.getIp());
        configuration.setPort(interfaceConfigDto.getPort());
        HostWebClient hostWebClient = HostWebClient.build(configuration);
        Map<String, Object> sendParam = new HashMap<>();
        sendParam.put("userUniqueId", userUniqueId);
        sendParam.put("action", action);
        LOGGER.debug("sendParam:[{}]", JsonUtil.toJson(sendParam));
        String json = hostWebClient.get("/permissionIfExists", sendParam);
        LOGGER.debug("json:{}", json);
        if (StringUtils.isEmpty(json))
            throw new Exception("response json is empty");
        Map<String, Object> response = JsonUtil.fromJson(Map.class, json);
        LOGGER.debug(response.get("msg").toString());
        return Boolean.parseBoolean(response.get("msg").toString());
    }
}
