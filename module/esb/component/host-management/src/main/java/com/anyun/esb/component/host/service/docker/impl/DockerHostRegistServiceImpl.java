/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.dto.*;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.JbiCommon;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.service.docker.DockerHostRegistService;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Version;
import org.apache.camel.CamelContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 4/26/16
 */
public class DockerHostRegistServiceImpl implements DockerHostRegistService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DockerHostRegistService.class);
    private HostBaseInfoDao hostBaseInfoDao = new HostBaseInfoDaoImpl();

    @Override
    public void connectHost(String host, String serialNumber, String managementIp) throws Exception {
//        String mip = hostBaseInfoDao.selectHostIdByIp(managementIp);
//        if(StringUtils.isNotEmpty(mip))
//            throw new Exception("Exist management ip [" + managementIp + "]");
        HostBaseInfoDto hostBaseInfoDto = hostBaseInfoDao.selectById(serialNumber);
        if (hostBaseInfoDto != null) {
            LOGGER.info("Host [{}] [{}] is a registed host,connection", serialNumber, managementIp);
            if (hostBaseInfoDto.getHostManagementIpInfoDto() != null) {
                hostBaseInfoDao.updateHostManagementIp(serialNumber, managementIp);
            }
            LOGGER.debug("Host [{}][{}] connected", host, serialNumber);
        } else {
            LOGGER.info("Host [{}] [{}] is not a registed host", serialNumber, managementIp);
            registHost(serialNumber, managementIp);
        }
        Jedis jedis = Env.env(Jedis.class);
        String registryDomain = jedis.get("com.anyun.docker.registry.url").replaceAll("https://", "");
        String hostCert = jedis.get("com.anyun.certs.path") + "/host_key";
        HostSshClient hostSshClient = new HostSshClient(hostCert);
        hostSshClient.setHost(managementIp);
        hostSshClient.connect();
        DockerClient dockerClient = Env.getDockerClient(managementIp);
        Env.export("host.docker." + serialNumber, dockerClient);
        Env.export("host.ssh." + serialNumber, hostSshClient);
        ServiceInvoker<List<Map>> invoker = new ServiceInvoker<>();
        ServiceInvoker.setCamelContext(Env.env(CamelContext.class));
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-registry");
        invoker.setService("image_query");
        Map<String,String>  m=new HashMap<>();
        m.put("subMethod","QUERY_REGISTRY");
        List<Map> imageDtos = invoker.invoke(m, null);
        LOGGER.debug("Registry image size --- [{}]", imageDtos.size());
        Version version = dockerClient.versionCmd().exec();
        LOGGER.debug("Docker version [{}]", JbiCommon.toJson(version));
        try {
            LOGGER.debug("uname [{}]", hostSshClient.exec("uname -a"));
        } catch (Exception ex) {
            LOGGER.debug("capcapca [{}]", ex.getMessage());
        }
        LOGGER.debug("issue [{}]", hostSshClient.exec("cat /etc/issue"));
        String docker = "docker";
        String passwd = jedis.get("com.anyun.docker.registry.passwd");
        String domain = jedis.get("com.anyun.docker.registry.url").replaceAll("https://", "");
        String user = jedis.get("com.anyun.docker.registry.user");
        String loginString = docker + " login -u " + user + " -p " + passwd + " " + domain;
        LOGGER.debug("Login String [{}]", loginString);
        String loginResult = hostSshClient.exec(loginString);
        LOGGER.debug("Login result [{}]", loginResult);
        for (int i = 0; i < imageDtos.size(); i++) {
            try {
                Map map = imageDtos.get(i);
                LOGGER.debug("Image [{}]", map);
                String pullName = registryDomain + "/" + map.get("category") + "/" + map.get("name") + ":" + map.get("tag");
                LOGGER.debug("Pull image [{}]", pullName);
                String pullResult = hostSshClient.exec(docker + " pull " + pullName);
                LOGGER.debug("Pull image [{}] finish [{}]", pullName, pullResult);
            } catch (Exception ex) {
                LOGGER.error("Image pull error [{}]", ex.getMessage(), ex);
            }
        }
        hostBaseInfoDao.updateDockerHostStatus(serialNumber, 1);
    }

    private void registHost(String serialNumber, String managementIp) throws Exception {
        Jedis jedis = Env.env(Jedis.class);
        String hostCert = jedis.get("com.anyun.certs.path") + "/host_key";
        HostSshClient hostSshClient = new HostSshClient(hostCert);
        hostSshClient.setHost(managementIp);
        hostSshClient.connect();
        LOGGER.debug("uname [{}]", hostSshClient.exec("uname -a"));
        LOGGER.debug("issue [{}]", hostSshClient.exec("cat /etc/issue"));
        HostExtInfoDto extInfoDto = getHostExtInfoFromRemote(hostSshClient);
        extInfoDto.setHostId(serialNumber);
        LOGGER.debug("Host Info [\n{}\n]", extInfoDto.asJson());
        DockerClient dockerClient = Env.getDockerClient(managementIp);
        Version version = dockerClient.versionCmd().exec();
        LOGGER.debug("Docker version [{}]", JbiCommon.toJson(version));
        hostBaseInfoDao.insertHostExtInfo(managementIp, extInfoDto);
        DockerHostInfoDto dockerHostInfoDto = hostBaseInfoDao.selectDockerHostDto(serialNumber);
        boolean mustUpdate = false;
        boolean mustAdd = false;
        if (dockerHostInfoDto == null)
            mustAdd = true;
        else if (!version.getVersion().equals(dockerHostInfoDto.getVersion()))
            mustUpdate = true;
        if (mustAdd || mustUpdate) {
            dockerHostInfoDto = new DockerHostInfoDto();
            dockerHostInfoDto.setHost(serialNumber);
            dockerHostInfoDto.setVersion(version.getVersion());
            dockerHostInfoDto.setOperatingSystem(version.getOperatingSystem());
            dockerHostInfoDto.setKernelVersion(version.getKernelVersion());
            dockerHostInfoDto.setGoVersion(version.getGoVersion());
            dockerHostInfoDto.setArch(version.getArch());
            dockerHostInfoDto.setGitCommit(version.getGitCommit());
        }
        if (mustAdd)
            hostBaseInfoDao.insertDockerHostInfo(dockerHostInfoDto);
        else if (mustUpdate)
            hostBaseInfoDao.updateDockerHostInfo(dockerHostInfoDto);
        hostBaseInfoDao.updateDockerHostStatus(serialNumber, 2);

        //插入网卡信息
        List<HostNetworkCardDto> hostNetworkCardDtoList = getHostNetworkCardDtoList(hostSshClient);
        for (HostNetworkCardDto card : hostNetworkCardDtoList) {
            if (card == null || StringUtils.isEmpty(card.getProduct()))
                continue;
            card.setHost(serialNumber);
            //查询
            HostNetworkCardDto h = hostBaseInfoDao.selectHostNetworkCardByHostAndMac(card.getId(), serialNumber);
            if (h == null) {
                //插入
                hostBaseInfoDao.insertHostNetworkCard(card);
            }
        }
        hostSshClient.getSession().disconnect();
        dockerClient.close();
    }


    @Override
    public List<Image> getAllDockerImages() throws Exception {
        //查询所有宿主机
        //查询所有宿主机的管理IP地址
        //初始化宿主机管理地址DockerClient
        //获取镜像信息
        List<HostBaseInfoDto> hosts = hostBaseInfoDao.selectByStatus(1);
        List<DockerClient> dockerClients = (List<DockerClient>) Env.env(List.class, "com.anyun.docker.clients");
        if (dockerClients == null) {
            dockerClients = new ArrayList<>();
            for (HostBaseInfoDto dto : hosts) {
                HostManagementIpInfoDto ipDto = dto.getHostManagementIpInfoDto();
                if (ipDto == null)
                    continue;
                String mgrIp = ipDto.getIpAddress();
                DockerClient client = Env.getDockerClient(mgrIp);
                dockerClients.add(client);
            }
            Env.set("com.anyun.docker.clients", dockerClients);
        }
        List<Image> allImages = new ArrayList<>();
        for (DockerClient _client : dockerClients) {
            List<Image> images = _client.listImagesCmd().exec();
            allImages.addAll(images);
        }
        return allImages;
    }

    @Override
    public DockerClient getDockerClient(String id) throws Exception {
        //TODO 获取一个已经连接到平台的HOST主机
        return null;
    }

    @Override
    public void disconnectDockerHost(String ip) {
        HostManagementIpInfoDto hostManagementIpInfoDto = hostBaseInfoDao.selectManagementIpDto(ip);
        if (hostManagementIpInfoDto == null)
            LOGGER.warn("Host [{}] not exist", ip);
        String serialNumber = hostManagementIpInfoDto.getHostId();
        hostBaseInfoDao.updateDockerHostStatus(serialNumber, 0);
        DockerClient dockerClient = Env.env(DockerClient.class, "host.docker." + serialNumber);
        HostSshClient hostSshClient = Env.env(HostSshClient.class, "host.ssh." + serialNumber);
        try {
            if (dockerClient != null)
                dockerClient.close();
            if (hostSshClient != null)
                hostSshClient.getSession().disconnect();
        } catch (Exception ex) {
        }
        Env.unset("host.docker." + serialNumber);
        Env.unset("host.ssh." + serialNumber);
        LOGGER.debug("Host [{}] disconnected", serialNumber);
    }

    private HostExtInfoDto getHostExtInfoFromRemote(HostSshClient hostSshClient) throws Exception {
        try {
            String content = hostSshClient.exec("/var/lib/anyuncloud-agent/script/info.sh").trim();
            HostExtInfoDto hostExtInfoDto = new HostExtInfoDto();
            List<String> contentList = StringUtils.readStringLines(content);
            hostExtInfoDto.setCpuModel(contentList.get(0));
            hostExtInfoDto.setCpuMhz(Double.valueOf(contentList.get(1)).longValue());
            hostExtInfoDto.setPhysicalCpus(Integer.valueOf(contentList.get(2)));
            hostExtInfoDto.setLogicalCpus(Integer.valueOf(contentList.get(3)));
            hostExtInfoDto.setCpuCores(Integer.valueOf(contentList.get(4)));
            if (contentList.get(5).contains("64"))
                hostExtInfoDto.setCpuPresentMode(1);
            else
                hostExtInfoDto.setCpuPresentMode(0);
            String disk = StringUtils.getSplitValues(contentList.get(6), " ")[1].replaceAll("G", "");
            hostExtInfoDto.setDockerDiskTotal(Long.valueOf(disk));
            hostExtInfoDto.setMemoryTotal(Long.valueOf(contentList.get(7)));
            return hostExtInfoDto;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    //获取宿主机网卡信息列表
    private List<HostNetworkCardDto> getHostNetworkCardDtoList(HostSshClient hostSshClient) throws Exception {
        String command = "lshw   -C  network   | awk  -F ''  'BEGIN {print \"begain\"}  {print $0 } END {print \"*-network\"}'\n ";
        String str = hostSshClient.exec(command).trim();
        File configFile = File.createTempFile("network", ".conf");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
        bufferedWriter.write(str);
        bufferedWriter.close();
        LOGGER.debug(":临时文件路径[{}]", configFile.getPath());
        BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
        String tempString = null;
        List<HostNetworkCardDto> l = new ArrayList<>();
        HostNetworkCardDto n = new HostNetworkCardDto();
        while ((tempString = bufferedReader.readLine()) != null) {
            if (tempString.contains("*-network")) {
                if (StringUtils.isNotEmpty(n.getProduct())) {
                    StringBuffer stringBuffer = new StringBuffer(n.getName().trim());
                    stringBuffer.append("-");
                    for (String mac : n.getMac().split(":")) {
                        stringBuffer.append(mac.trim());
                    }
                    n.setId(stringBuffer.toString().trim().toUpperCase());
                    l.add(n);
                }
                n = new HostNetworkCardDto();
                continue;
            }

            if (tempString.contains("description:")) {
                n.setDescription(tempString.split(":")[1].toString());
                continue;
            }

            if (tempString.contains("product:")) {
                n.setProduct(tempString.split(":")[1].trim());
                continue;
            }

            if (tempString.contains("vendor:")) {
                n.setVendor(tempString.split(":")[1].trim());
                continue;
            }

            if (tempString.contains("logical name:")) {
                n.setName(tempString.split(":")[1].trim());
                continue;
            }

            if (tempString.contains("serial:")) {
                n.setMac(tempString.trim().split(" ")[1].trim());
                continue;
            }

            if (tempString.contains("size:")) {
                n.setSpeed(tempString.split(":")[1].trim());
                continue;
            }
        }
        LOGGER.debug("l:[{}]", JsonUtil.toJson(l));
        if (configFile.exists()) {
            configFile.delete();
        }
        return l;
    }
}