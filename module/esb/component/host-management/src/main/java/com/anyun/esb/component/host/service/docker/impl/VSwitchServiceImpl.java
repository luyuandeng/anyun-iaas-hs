package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.ContainerDto;
import com.anyun.cloud.dto.HostBaseInfoDto;
import com.anyun.cloud.dto.HostManagementIpInfoDto;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.service.docker.DockerHostService;
import com.anyun.esb.component.host.service.docker.VSwitchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by twitchgg on 16-7-19.
 */
public class VSwitchServiceImpl implements VSwitchService {
    private static final Logger LOGGER = LoggerFactory.getLogger(VSwitchServiceImpl.class);
    private DockerHostService dockerHostService;
    private ContainerDao containerDao;

    public VSwitchServiceImpl() {
        dockerHostService = new DockerHostServiceImpl();
        containerDao = new ContainerDaoImpl();
    }

    //查询 指定名称 和 和类型的网络 标签
    @Override
    public String getNetworkLabel(String id, String type) throws Exception {
        List<String> activeHosts = dockerHostService.findAllActiveHosts();
        if (activeHosts == null || activeHosts.isEmpty())
            throw new Exception("Could not find active hosts");
        String serialNumber = activeHosts.get(0).split(":")[0].trim();
        LOGGER.debug("Find host serial number [{}]", serialNumber);
        HostSshClient hostSshClient = Env.getSshClient(activeHosts.get(0));
        if (hostSshClient == null)
            throw new Exception("Could not find active host [" + serialNumber + "] ssh client");
        String command = "docker network ls | grep " + type + " | awk -F ' ' '{printf \"%s:%s\\n\", $3,$2}'";
        LOGGER.debug("find docker network label cmd [{}]", command);
        String result = hostSshClient.exec(command);
        LOGGER.debug("Network list result [{}]", result);
        List<String> labels = StringUtils.readStringLines(result);
        for (String label : labels) {
            LOGGER.debug("OVN docker network label [{}]", label);
            String _label = type + ":" + id;
            LOGGER.debug("label [{}] | [{}]", _label, label);
            if (label.equals(_label)) {
                return _label;
            }
        }
        return null;
    }

    //获取所有网络标签
    @Override
    public List<String> getAllNetworkLabel() throws Exception {
        List<String> l = new ArrayList<>();
        List<String> activeHosts = dockerHostService.findAllActiveHosts();
        if (activeHosts == null || activeHosts.isEmpty())
            throw new Exception("Could not find active hosts");
        String serialNumber = activeHosts.get(0).split(":")[0].trim();
        LOGGER.debug("Find host serial number [{}]", serialNumber);
        HostSshClient hostSshClient = Env.getSshClient(activeHosts.get(0));
        if (hostSshClient == null)
            throw new Exception("Could not find active host [" + serialNumber + "] ssh client");
        String command = "docker network ls  | awk -F ' ' '{printf \"%s:%s\\n\", $3,$2}'";
        String result = hostSshClient.exec(command);
        List<String> labels = StringUtils.readStringLines(result);
        if (labels.size() <= 1) {
            LOGGER.debug("没有网络标签");
            return l;
        }
        labels.remove(0);
        for (String label : labels) {
            if (label.split(":")[0].matches("openvswitch") || label.split(":")[0].matches("bridge")) {
                l.add(label.split(":")[1]);
                LOGGER.debug("OVN docker network label [{}]", label);
            }
        }
        return l;
    }

    //判断标签是否存在
    @Override
    public boolean networkLabelExist(String label, String type) throws Exception {
        Boolean flag = true;
        try {
            String _label = getNetworkLabel(label, type);
            if (StringUtils.isNotEmpty(_label))
                flag = true;
            else
                flag = false;
        } catch (Exception e) {
            LOGGER.warn("find network label error", e);
            flag = false;
        } finally {
            LOGGER.debug("flag:[{}]", flag);
            return flag;
        }
    }

    //创建 网络标签(openvswitch)
    @Override
    public void createOVNNetwork(String label, String subnet, String gateway) throws Exception {
        if (networkLabelExist(label, "openvswitch"))
            throw new Exception("Network label [" + label + "] exist");
        List<String> activeHosts = dockerHostService.findAllActiveHosts();
        LOGGER.debug("activeHosts" + activeHosts);
        if (activeHosts == null || activeHosts.isEmpty())
            throw new Exception("Could not find active hosts");
        String serialNumber = activeHosts.get(0).split(":")[0].trim();
        LOGGER.debug("Find host serial number [{}]", serialNumber);
        HostSshClient hostSshClient = Env.getSshClient(activeHosts.get(0));
        if (hostSshClient == null)
            throw new Exception("Could not find active host [" + serialNumber + "] ssh client");
        StringBuilder sb = new StringBuilder("docker network create --internal");
        sb.append(" --subnet=").append(subnet);
        sb.append(" -d openvswitch");
        if (StringUtils.isNotEmpty(gateway))
            sb.append(" --gateway=").append(gateway);
        sb.append(" " + label);
        LOGGER.debug("Network create command [{}]", sb.toString());
        hostSshClient.exec(sb.toString());
    }

    //删除网络标签（type：openvswitch）
    @Override
    public void deleteOVNNetwork(String label) throws Exception {
        if (!networkLabelExist(label, "openvswitch"))
            throw new Exception("Network label [" + label + "] not exist");
        List<String> activeHosts = dockerHostService.findAllActiveHosts();
        if (activeHosts == null || activeHosts.isEmpty())
            throw new Exception("Could not find active hosts");
        String serialNumber = activeHosts.get(0).split(":")[0].trim();
        LOGGER.debug("Find host serial number [{}]", serialNumber);
        HostSshClient hostSshClient = Env.getSshClient(activeHosts.get(0));
        if (hostSshClient == null)
            throw new Exception("Could not find active host [" + serialNumber + "] ssh client");

        //查询是否有 容器在上面
//        String command1 = " docker network inspect  --format='{{range $p, $conf := .Containers}}  {{$p}}  {{end}}' " + label;
//        LOGGER.debug("command1 is:{}", command1);
//        String str = hostSshClient.exec(command1).trim();
//        if (StringUtils.isNotEmpty(str)) {
//            List<String> list = Arrays.asList(str.split(" "));
//            for (String c : list) {
//                if (StringUtils.isEmpty(c))
//                    continue;
//                try{
//                    String command2 = "docker network  disconnect  -f" + " " + label + " " + c;
//                    hostSshClient.exec(command2);
//                }catch(Exception e){
//                    LOGGER.debug("exception:[{}]",e);
//                }
//            }
//        }
        String command3 = "docker network  rm" + "  " + label;
        hostSshClient.exec(command3);

    }

    //容器连接到网络（tyoe：openvswitch）
    @Override
    public void connectToNetwork(String containerId, String type, String label) throws Exception {
        if (!networkLabelExist(label, type))
            throw new Exception("Network [" + type + "] label [" + label + "] not exist");
        ContainerDto containerDto = containerDao.selectContainerById(containerId);
        if (containerDto == null || StringUtils.isEmpty(containerDto.getId()))
            throw new Exception("Container[ " + containerId + "] not exist");
        HostBaseInfoDao hostBaseInfoDao=new HostBaseInfoDaoImpl();
        HostManagementIpInfoDto hostManagementIpInfoDto= hostBaseInfoDao.selectManagementIpDtoByHost(containerDto.getHostId());
        String serialNumber = containerDto.getHostId();
        String mgrIp = hostManagementIpInfoDto.getIpAddress();
        HostSshClient hostSshClient = Env.getSshClient(serialNumber + ":" + mgrIp);
        String command = "docker network connect " + label + " " + containerId;
        hostSshClient.exec(command);
    }

    //容器连接到网络（type：bridge）
    @Override
    public void connectToNetwork(String containerId, String type, String label, String ip) throws Exception {
        if (!networkLabelExist(label, type))
            throw new Exception("Network [" + type + "] label [" + label + "] not exist");
        ContainerDto containerDto = containerDao.selectContainerById(containerId);
        if (containerDto == null || StringUtils.isEmpty(containerDto.getId()))
            throw new Exception("Container[ " + containerId + "] not exist");
        HostBaseInfoDao hostBaseInfoDao=new HostBaseInfoDaoImpl();
        HostManagementIpInfoDto  hostManagementIpInfoDto=hostBaseInfoDao.selectManagementIpDtoByHost(containerDto.getHostId());
        String serialNumber =containerDto.getHostId();
        String mgrIp = hostManagementIpInfoDto.getIpAddress();
        HostSshClient hostSshClient = Env.getSshClient(serialNumber + ":" + mgrIp);
        String command = "docker network connect  --ip" + " " + ip + " " + label + " " + containerId;
        hostSshClient.exec(command);
    }


    //查询  ip和mac地址
    @Override
    public Map<String, Object> queryContainerIpAddress(String label, String containerId) throws Exception {
        ContainerDto containerDto = containerDao.selectContainerById(containerId);
        LOGGER.debug("Container [{}] short id [{}]", containerId, containerDto.getShortId());
        HostBaseInfoDao hostBaseInfoDao=new HostBaseInfoDaoImpl();
         HostManagementIpInfoDto hostManagementIpInfoDto = hostBaseInfoDao.selectManagementIpDtoByHost(containerDto.getHostId());
        String serialNumber = containerDto.getHostId();
        String mgrIp = hostManagementIpInfoDto.getIpAddress();
        HostSshClient hostSshClient = Env.getSshClient(serialNumber + ":" + mgrIp);
        String command = "docker network inspect " +
                "--format='{{range $p, $conf := .Containers}} " +
                "{{$p}}-{{$conf.Name}}-{{$conf.IPv4Address}}-{{$conf.MacAddress}} {{end}}' " + label;
        LOGGER.debug("command is:{}", command);
        String str = hostSshClient.exec(command).trim();
        LOGGER.debug("Inspect docker network information [{}]", str);
        //41670abfbb0dc44562acead9f74acd3e5693c6391880565d8c48fbc44f1f3e53-nRb92wEmaav-172.20.0.5/16-02:e7:11:6e:7a:d7
        //  docker network inspect --format='{{range $p, $conf:= .Containers}}   {{$p}}-{{$conf.Name}}-{{$conf.IPv4Address}}-{{$conf.MacAddress}} {{end}}'  anyun-in
        String[] strings = str.split(" ");
        Map<String, Object> m = new HashMap<String, Object>();
        for (String string : strings) {
            if (StringUtils.isEmpty(string))
                continue;
            if (string.split("-")[0].equals(containerId)) {
                m.put("ip", string.split("-")[2].split("/")[0]);
                m.put("mac", string.split("-")[3]);
                break;
            }
        }
        LOGGER.debug("m is:{}", JsonUtil.toJson(m));
        return m;
    }

    //从网络标签移除去移除容器
    @Override
    public void disconnectFromNetwork(String containerId, String type, String label) throws Exception {
        if (!networkLabelExist(label, type))
            throw new Exception("Network [" + type + "] label [" + label + "] not exist");
        ContainerDto containerDto = containerDao.selectContainerById(containerId);
        if (containerDto == null || StringUtils.isEmpty(containerDto.getId()))
            throw new Exception("Container[ " + containerId + "] not exist");
        HostBaseInfoDao hostBaseInfoDao=new HostBaseInfoDaoImpl();
        HostManagementIpInfoDto hostManagementIpInfoDto=hostBaseInfoDao.selectManagementIpDtoByHost(containerDto.getHostId());
        String serialNumber = hostManagementIpInfoDto.getHostId();
        String mgrIp = hostManagementIpInfoDto.getIpAddress();
        HostSshClient hostSshClient = Env.getSshClient(serialNumber + ":" + mgrIp);
        String command = "docker network disconnect " + label + " " + containerDto.getShortId();
        hostSshClient.exec(command);
    }
}
