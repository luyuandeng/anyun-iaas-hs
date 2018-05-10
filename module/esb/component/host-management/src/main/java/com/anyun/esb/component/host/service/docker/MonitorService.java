package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.MonitorContainerDto;
import com.anyun.cloud.dto.MonitorHostDto;

import java.util.List;

/**
 * Created by gp on 16-9-18.
 */
public interface MonitorService {
    /**
     * 宿主机监控
     * @param host
     * @return
     * @throws Exception
     */
    List<MonitorHostDto> queryHostInformation(String host)  throws Exception;

    /**
     * 容器监控
     * @param container
     * @return
     */
    List<MonitorHostDto> queryContainerInformation(String container) throws Exception;

    /**
     * 根据宿主机查询容器
     * @param host
     * @return
     * @throws Exception
     */
    MonitorContainerDto queryContainerByHost(String host) throws Exception;

    /**
     * 根据宿主机ID查询容器详情
     * @param hostid
     * @return
     * @throws Exception
     */
    List<ContainerDto> queryContainerInfoByHostId(String hostid) throws Exception;

    /**
     * 监控查询
     * @param subMethod
     *
     *
     * @param subParameters
     * @return
     *  @throws Exception
     */
    List<MonitorHostDto> queryMonitor( String subMethod, String subParameters)throws Exception;

    List<MonitorHostDto> getMonitorHistoryData(String subMethod, String subParameters) throws Exception;

    void deleteMonitorData(String endTime)throws Exception;
}
