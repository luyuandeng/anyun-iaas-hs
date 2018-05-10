package com.anyun.sdk.platfrom.core.rest.impl;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.MonitorHostDto;
import com.anyun.cloud.dto.MonitorOverviewDto;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.sdk.platfrom.MonitorService;
import com.anyun.sdk.platfrom.core.rest.AnyunSdkClientFactory;
import com.anyun.sdk.platfrom.core.rest.ResourceClient;
import com.anyun.sdk.platfrom.exception.RestfulApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-9-21.
 */
public class MonitorServiceImpl implements MonitorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorServiceImpl.class);
    public static final String PATH_OVERVIEW_DETAILS = "/monitor/overview/details";
    public static final String PATH_QUERY_MONITOR = "/monitor/list";
    public static final String PATH_QUERY_HISTORY_DATA = "monitor/list/historyData";
    public static final String PATH_DELETE_MONITOR = "monitor/delete";

    /**
     * 总览监控
     *
     * @return
     * @throws RestfulApiException
     */
    @Override
    public MonitorOverviewDto queryOverviewMonitor(String userUniqueId) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_OVERVIEW_DETAILS, params);
        MonitorOverviewDto requests = ResourceClient.convertToAnyunEntity(MonitorOverviewDto.class, response);
        return requests;
    }

    /**
     * 监控查询
     *
     * @param subMethod     String   true    子方法
     *                      queryHostMonitor：宿主机监控
     *                      queryContainerMonitor :容器监控
     * @param subParameters 子参数  格式如  id|label
     * @return
     * @throws Exception
     */
    public List<MonitorHostDto> queryMonitor(String userUniqueId, String subMethod, String subParameters) throws RestfulApiException {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("subParameters", subParameters);
        params.put("subMethod", subMethod);
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_MONITOR, params);
        List<MonitorHostDto> dto = ResourceClient.convertToListObject(MonitorHostDto.class, response);
        return dto;
    }

    @Override
    public List<MonitorHostDto> getMonitorHistoryData(String userUniqueId, String subMethod, String subParameters) throws Exception {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("subParameters", subParameters);
        params.put("subMethod", subMethod);
        params.put("userUniqueId", userUniqueId);
        String response = rsClient.query(PATH_QUERY_HISTORY_DATA, params);
        List<MonitorHostDto> dto = ResourceClient.convertToListObject(MonitorHostDto.class, response);
        return dto;
    }

    @Override
    public Status<Boolean> deleteMonitorData(String userUniqueId, Date endTime) throws Exception {
        ResourceClient rsClient = AnyunSdkClientFactory.getFactory().getResourceClient();
        Map<String, Object> params = new HashMap<>();
        params.put("userUniqueId", userUniqueId);
        params.put("endTime", endTime);
        LOGGER.debug("params:[{}]" , JsonUtil.toJson(params));
        String response = rsClient.delete(PATH_DELETE_MONITOR, params);
        Status<Boolean> statusDto = ResourceClient.convertToAnyunEntity(Status.class, response);
        return statusDto;
    }
}
