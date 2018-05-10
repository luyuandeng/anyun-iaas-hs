package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.api.Response;
import com.anyun.cloud.dto.*;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.OverviewMonitorDao;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.dao.impl.OverviewMonitorDaoImpl;
import com.anyun.esb.component.host.service.docker.DockerHostService;
import com.anyun.esb.component.host.service.docker.OverviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-9-13.
 */
public class OverviewServiceImpl implements OverviewService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OverviewServiceImpl.class);
    private DockerHostService dockerHostService = new DockerHostServiceImpl();
    private HostBaseInfoDao hostBaseInfoDao = new HostBaseInfoDaoImpl();
    private OverviewMonitorDao overviewMonitorDao = new OverviewMonitorDaoImpl();


    List<DockerImageDto> DockerImageQueryRegistryService(String userUniqueId) throws Exception {
        ServiceInvoker invoker = new ServiceInvoker<>();
        invoker.setComponent("anyun-registry");
        invoker.setService("image_query");
        Map<String, String> param = new HashMap<String, String>();
        param.put("userUniqueId", userUniqueId);
        param.put("subMethod","QUERY_REGISTRY");
        String json = invoker.invoke1(param, null);
        Response<List<DockerImageDto>> response = JsonUtil.fromJson(Response.class, json);
        List<DockerImageDto> s = response.getContent();
        return s;
    }

    List<DockerImageDto> DockerImageQueryUnregistryService(String userUniqueId) throws Exception {
        ServiceInvoker invoker = new ServiceInvoker<>();
        invoker.setComponent("anyun-registry");
        invoker.setService("image_query");
        Map<String, String> param = new HashMap<String, String>();
        param.put("userUniqueId", userUniqueId);
        param.put("subMethod","QUERY_UNREGISTRY");
        String json = invoker.invoke1(param, null);
        Response<List<DockerImageDto>> response = JsonUtil.fromJson(Response.class, json);
        List<DockerImageDto> s = response.getContent();
        return s;
    }

    Map<String, Object> StorageRealTimeDataQueryService() throws Exception{
        ServiceInvoker invoker = new ServiceInvoker<>();
        invoker.setComponent("anyun-storage");
        invoker.setService("storage_realTimeData_query");
        String json = invoker.invoke1(null, null);
        LOGGER.debug("Json:"+json);
        Response<Map<String,Object>> response = JsonUtil.fromJson(Response.class,json);
        Map<String,Object> dto = response.getContent();
        return  dto;
    }
    /**
     * 总览监控
     * @return
     * @throws Exception
     */
    @Override
    public MonitorOverviewDto queryOverviewMonitor(String userUniqueId) throws Exception {
        //查询所有宿主机
        List<HostBaseInfoDto> totalhosts = hostBaseInfoDao.selectAllHostInfo();
        //查询所有活动宿主机
        List<String> host = dockerHostService.findAllActiveHosts();
        //查询所有容器数量（包含运行和停止中的容器）
        MonitorOverviewDto dto =new MonitorOverviewDto();
        Map<String,Object> map = overviewMonitorDao.selectContainerCount();
        //查询卷数量
        Map<String,Object> volumeMap = overviewMonitorDao.selectVolumeCount();
        dto.setRunningContainer(Integer.parseInt(map.get("runningContainer").toString()));
        dto.setStopContainer(Integer.parseInt(map.get("stopContainer").toString()));
        int totalContainer = Integer.parseInt(map.get("runningContainer").toString())+Integer.parseInt(map.get("stopContainer").toString());
        Map<String,Object> map1 = StorageRealTimeDataQueryService();
        LOGGER.debug("map1:"+JsonUtil.toJson(map1));
        dto.setSize(map1.get("size") == null ? "" : map1.get("size").toString());
        dto.setUsed(map1.get("used") == null ? "": map1.get("used").toString());
        dto.setAvail(map1.get("avail") == null ? "" :map1.get("avail").toString());
        dto.setUsedPercentage(map1.get("usedPercentage") == null ? "" : map1.get("usedPercentage").toString());
        dto.setTotalHost(totalhosts.size());
        dto.setRuningHost(host.size());
        dto.setStopHost(totalhosts.size()-host.size());
        dto.setTotalContainer(totalContainer);
        List<DockerImageDto> list = DockerImageQueryRegistryService(userUniqueId);
        List<DockerImageDto> list1 = DockerImageQueryUnregistryService(userUniqueId);
        dto.setRegistryImages(list.size());
        dto.setUnregistryImages(list1.size());
        int totaoImage=list.size()+list1.size();
        dto.setTotalImages(totaoImage);
        dto.setTotalVolume(Integer.parseInt(volumeMap.get("total").toString()));
        dto.setAttachedVolume(Integer.parseInt(volumeMap.get("volume").toString()));
        int detachedVolume = Integer.parseInt(volumeMap.get("total").toString())-Integer.parseInt(volumeMap.get("volume").toString());
        dto.setDetachedVolume(detachedVolume);
        return dto;
    }
}
