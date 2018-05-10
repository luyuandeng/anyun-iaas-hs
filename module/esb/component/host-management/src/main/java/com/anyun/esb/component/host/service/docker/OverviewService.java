package com.anyun.esb.component.host.service.docker;

import com.anyun.cloud.dto.MonitorOverviewDto;

/**
 * Created by gp on 16-9-13.
 */
public interface OverviewService {
    //总览监控
    MonitorOverviewDto queryOverviewMonitor(String userUniqueId) throws Exception;
}
