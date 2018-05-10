package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.common.HostWebClient;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.OverviewMonitorDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.dao.impl.OverviewMonitorDaoImpl;
import com.anyun.esb.component.host.service.docker.ContainerService;
import com.anyun.esb.component.host.service.docker.InterfaceService;
import com.anyun.esb.component.host.service.docker.MonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gp on 16-9-18.
 */
public class MonitorServiceImpl implements MonitorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MonitorServiceImpl.class);
    private OverviewMonitorDao overviewMonitorDao = new OverviewMonitorDaoImpl();
    private ContainerDao containerDao = new ContainerDaoImpl();
    private ContainerService containerService = new ContainerServiceImpl();
    private InterfaceService interfaceService = new InterfaceServiceImpl();


    /**
     * 宿主机监控
     *
     * @param host
     * @return
     * @throws Exception
     */
    @Override
    public List<MonitorHostDto> queryHostInformation(String host) throws Exception {
        HostWebClient.Configuration configuration = new HostWebClient.Configuration();
        String name = "logger";
        InterfaceConfigDto interfaceConfigDto = interfaceService.queryInterfaceConfig(name);
        configuration.setPlatformAddress(interfaceConfigDto.getIp());
        HostWebClient hostWebClient = HostWebClient.build(configuration);
        String hostIp = host.replace(".", "&");
        LOGGER.debug("转义后的hostIp：" + JsonUtil.toJson(hostIp));
        Map<String, Object> m = new HashMap<String, Object>() {{
            put("host", hostIp);
        }};
        String json = hostWebClient.get("/getmachinestats/" + hostIp, m);
        LOGGER.debug("Json length :[{}]", json.length());
        List<Map<String, Object>> l = JsonUtil.fromJson(List.class, json);
        if (l == null || l.isEmpty()) {
            return new ArrayList<>();
        }
        List<MonitorHostDto> list = new ArrayList<>();
        for (Map<String, Object> map : l) {
            MonitorHostDto dto = new MonitorHostDto();
            dto.setCpu(map.get("cpuusage") == null ? null : map.get("cpuusage").toString());
            Map<String, Object> mem = (Map<String, Object>) map.get("memory");
            MemoryDto memoryDto = new MemoryDto();
            memoryDto.setUsage(mem.get("usage") == null ? null : mem.get("usage").toString());
            memoryDto.setMemoryCapacity(mem.get("memory_capacity") == null ? null : mem.get("memory_capacity").toString());
            dto.setMemory(memoryDto);

            dto.setDate(map.get("timestamp") == null ? null : map.get("timestamp").toString());
            List<Map<String, Object>> net = (List<Map<String, Object>>) map.get("network");
            List<NetMonitorDto> network = new ArrayList<>();
            for (Map<String, Object> map1 : net) {
                NetMonitorDto netDto = new NetMonitorDto();
                netDto.setName(map1.get("name") == null ? null : map1.get("name").toString());
                netDto.setRxBytes(map1.get("rx_bytes") == null ? null : map1.get("rx_bytes").toString());
                netDto.setTxBytes(map1.get("tx_bytes") == null ? null : map1.get("tx_bytes").toString());
                network.add(netDto);
            }
            SystemUptimeDto systemUptimeDto = containerService.getHostSystemUptime(host);
            dto.setSystemUptimeDto(systemUptimeDto);
            dto.setNetwork(network);
            List<Map<String, Object>> file = (List<Map<String, Object>>) map.get("filesystem");
            List<FileSystemDto> filesystem = new ArrayList<>();
            for (Map<String, Object> map2 : file) {
                FileSystemDto fileSystemDto = new FileSystemDto();
                fileSystemDto.setUsage(map2.get("usage") == null ? null : map2.get("usage").toString());
                fileSystemDto.setCapacity(map2.get("capacity") == null ? null : map2.get("capacity").toString());
                fileSystemDto.setDevice(map2.get("device") == null ? null : map2.get("device").toString());
                filesystem.add(fileSystemDto);
            }
            dto.setFilesystem(filesystem);
            list.add(dto);
        }

        return list;
    }

    /**
     * 容器监控
     *
     * @param container
     * @return
     */
    @Override
    public List<MonitorHostDto> queryContainerInformation(String container) throws Exception {
        HostWebClient.Configuration configuration = new HostWebClient.Configuration();
        String name = "logger";
        InterfaceConfigDto interfaceConfigDto = interfaceService.queryInterfaceConfig(name);
        configuration.setPlatformAddress(interfaceConfigDto.getIp());
        HostWebClient hostWebClient = HostWebClient.build(configuration);
        ContainerDto containerDto = containerDao.selectContainerById(container);

        HostBaseInfoDao  hostBaseInfoDao=new HostBaseInfoDaoImpl();
        HostManagementIpInfoDto  hostManagementIpInfoDto=hostBaseInfoDao.selectManagementIpDtoByHost(containerDto.getHostId());
        String hostIp = hostManagementIpInfoDto.getIpAddress();
        String hostIp1 = hostIp.replace(".", "&");
        Map<String, Object> m = new HashMap<String, Object>() {{
            put("container", container);
            put("hostIp1", hostIp1);
        }};
        LOGGER.debug("容器ID:" + m);
        String json = hostWebClient.get("/getcontainerdocker/" + hostIp1 + "/" + container, m);
        LOGGER.debug("Json length :[{}]", json.length());
        List<Map<String, Object>> l = JsonUtil.fromJson(List.class, json);
        if (l == null || l.isEmpty()) {
            return new ArrayList<>();
        }
        List<MonitorHostDto> list = new ArrayList<>();
        for (Map<String, Object> map : l) {
            MonitorHostDto dto = new MonitorHostDto();
            dto.setCpu(map.get("cpuusage") == null ? null : map.get("cpuusage").toString());
            Map<String, Object> mem = (Map<String, Object>) map.get("memory");
            MemoryDto memoryDto = new MemoryDto();
            memoryDto.setUsage(mem.get("usage") == null ? null : mem.get("usage").toString());
            memoryDto.setMemoryCapacity(mem.get("memory_capacity") == null ? null : mem.get("memory_capacity").toString());
            dto.setMemory(memoryDto);
            dto.setDate(map.get("timestamp") == null ? null : map.get("timestamp").toString());
            List<Map<String, Object>> net = (List<Map<String, Object>>) map.get("network");
            List<NetMonitorDto> network = new ArrayList<>();
            for (Map<String, Object> map1 : net) {
                NetMonitorDto netDto = new NetMonitorDto();
                netDto.setName(map1.get("name") == null ? null : map1.get("name").toString());
                netDto.setRxBytes(map1.get("rx_bytes") == null ? null : map1.get("rx_bytes").toString());
                netDto.setTxBytes(map1.get("tx_bytes") == null ? null : map1.get("tx_bytes").toString());
                network.add(netDto);
            }
            SystemUptimeDto systemUptimeDto = containerService.getContainerSystemUptime(container);
            dto.setSystemUptimeDto(systemUptimeDto);
            dto.setNetwork(network);
            List<Map<String, Object>> file = (List<Map<String, Object>>) map.get("filesystem");
            List<FileSystemDto> filesystem = new ArrayList<>();
            for (Map<String, Object> map2 : file) {
                FileSystemDto fileSystemDto = new FileSystemDto();
                fileSystemDto.setUsage(map2.get("usage") == null ? null : map2.get("usage").toString());
                fileSystemDto.setCapacity(map2.get("capacity") == null ? null : map2.get("capacity").toString());
                fileSystemDto.setDevice(map2.get("device") == null ? null : map2.get("device").toString());
                filesystem.add(fileSystemDto);
            }
            dto.setFilesystem(filesystem);
            list.add(dto);
        }
        return list;
    }

    /**
     * 根据宿主机查询容器
     *
     * @param host
     * @return
     * @throws Exception
     */
    @Override
    public MonitorContainerDto queryContainerByHost(String host) throws Exception {
        Map<String, Object> map = overviewMonitorDao.selectContainerByHost(host);
        MonitorContainerDto dto = new MonitorContainerDto();
        dto.setRunningContainer(Integer.parseInt(map.get("runningContainer").toString()));
        dto.setStopContainer(Integer.parseInt(map.get("stopContainer").toString()));
        return dto;
    }

    /**
     * 根据宿主机ID查询容器详情
     *
     * @param hostid
     * @return
     * @throws Exception
     */
    @Override
    public List<ContainerDto> queryContainerInfoByHostId(String hostid) throws Exception {
        List<ContainerDto> list = overviewMonitorDao.selectContainerInfoByHostId(hostid);
        return list;
    }


    /**
     * 监控查询
     *
     * @param subMethod
     * @param subParameters
     * @return
     * @throws Exception
     */
    public List<MonitorHostDto> queryMonitor(String subMethod, String subParameters) throws Exception {
        List<MonitorHostDto> l = null;
        switch (subMethod) {
            case "QUERY_BY_HOST":
                l = queryHostInformation(subParameters);
                break;

            case "QUERY_BY_CONTAINER":
                l = queryContainerInformation(subParameters);
                break;
            default:
                throw new Exception("subMethod:[" + subMethod + "]  does not exist");
        }
        return l;
    }

    @Override
    public List<MonitorHostDto> getMonitorHistoryData(String subMethod, String subParameters) throws Exception {
        List<MonitorHostDto> l = null;
        String[] s = subParameters.replace("|", ",").split(",");
        if (s.length < 3)
            throw new Exception("SubParameters format  error");
        String id = s[0];
        if (StringUtils.isEmpty(id))
            throw new Exception("The first param is empty");
        String startTime = s[1];
        if (StringUtils.isEmpty(startTime))
            throw new Exception("The second param is empty");
        String endTime = s[2];
        if (StringUtils.isEmpty(endTime))
            throw new Exception("The third param is empty");

        switch (subMethod) {
            case "GET_HOST_MONITOR_HISTORY_DATA":
                String hostIp = id;
                l = getHostMonitorHistoryData(hostIp, startTime, endTime);
                break;
            case "GET_CONTAINER_MONITOR_HISTORY_DATA":
                String containerId = id;
                l = getContainerMonitorHistoryData(containerId, startTime, endTime);
                break;
            default:
                throw new Exception("subMethod:[" + subMethod + "]  does not exist");
        }
        return l;
    }

    @Override
    public void deleteMonitorData(String endTime) throws Exception {
        //TODO
    }

    private List<MonitorHostDto> getHostMonitorHistoryData(String hostId, String startTime, String endTime) throws Exception{
        return  queryHostInformation(hostId);
    }

    private List<MonitorHostDto> getContainerMonitorHistoryData(String containerId, String startTime, String endTime) throws Exception{
        return queryContainerInformation(containerId);
    }
}
