package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Date;

/**
 * Created by gp on 16-9-21.
 */
@Path("/monitor")
public class MonitorApi {
    /**
     * 1、总览 监控 详情
     *
     * @return MonitorOverviewDto
     */
    @GET
    @Path("/overview/details")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "query.overview.monitor",
            service = "query_overview_monitor"
    )
    public String getOverviewMonitorDetails(String userUniqueId) {
        return null;
    }

    /**
     * 2、查询 监控列表
     *
     * @param userUniqueId  String   false   用户标识
     * @param subMethod     String   true    子方法
     *                      QUERY_BY_HOST：  宿主机监控
     *                      host  宿主机 ip
     *                      QUERY_BY_CONTAINER :容器监控
     *                      container 容器Id
     * @param subParameters 子参数  格式如  id|label
     * @return List<MonitorHostDto>
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    @RestMethodDefine(
            needAuthentication = true,
            component = "anyun-host",
            operate = "query.monitor",
            service = "query_monitor")
    public String getList(String userUniqueId, String subMethod, String subParameters) {
        return null;
    }


    /**
     * 3、查询监控历史数据
     *
     * @param userUniqueId  String   false   用户标识
     * @param subMethod     String   true    子方法
     *                      GET_HOST_MONITOR_HISTORY_DATA：获取宿主机监控历史数据
     *                      hostIp             宿主机管理ip
     *                      beginTime          开始时间
     *                      endTime            结束时间
     *                      GET_CONTAINER_MONITOR_HISTORY_DATA :获取容器监控历史数据
     *                      containerId          容器Id
     *                      beginTime          开始时间
     *                      endTime            结束时间
     * @param subParameters 子参数  格式如        hostIp|beginTime|endTime
     * @return List<MonitorHostDto>
     */
    @GET
    @Path("/list/historyData")
    @Produces("application/json")
    @RestMethodDefine(
            needAuthentication = true,
            component = "anyun-host",
            operate = "query.monitor.history.data",
            service = "query_monitor_history_data")
    public String getMonitorHistoryData(String userUniqueId, String subMethod, String subParameters) {
        return null;
    }


    /**
     * 4、删除监控数据 （删除结束时间之前的所有数据）
     *
     * @param userUniqueId String   false   用户标识
     * @param endTime      Date  true   结束时间
     * @return Status<Boolean>
     * true :成功
     * false:失败
     */
    @DELETE
    @Path("/delete")
    @Produces("application/json")
    @RestMethodDefine(
            needAuthentication = true,
            component = "anyun-host",
            operate = "delete.monitor.data",
            service = "delete_monitor_data")
    public String deleteMonitorData(String userUniqueId, Date endTime) {
        return null;
    }
}


