package com.anyun.sdk.platfrom;

import com.anyun.cloud.api.Status;
import com.anyun.cloud.dto.MonitorHostDto;
import com.anyun.cloud.dto.MonitorOverviewDto;
import com.anyun.sdk.platfrom.exception.RestfulApiException;

import java.util.Date;
import java.util.List;

/**
 * Created by gp on 16-9-21.
 */
public interface MonitorService {

    /**
     * 1、总览监控
     *
     * @return
     * @throws RestfulApiException
     */
    MonitorOverviewDto queryOverviewMonitor(String userUniqueId) throws RestfulApiException;

    /**
     * 2、查询 监控列表
     *
     * @param subMethod     String   true    子方法
     *                      queryHostMonitor：宿主机监控
     *                      queryContainerMonitor :容器监控
     * @param subParameters 子参数  格式如  id|label
     * @return
     * @throws Exception
     */
    List<MonitorHostDto> queryMonitor(String userUniqueId, String subMethod, String subParameters) throws Exception;

    List<MonitorHostDto>  getMonitorHistoryData(String userUniqueId, String subMethod, String subParameters) throws Exception;

    Status<Boolean> deleteMonitorData(String userUniqueId,Date endTime) throws Exception;
}
