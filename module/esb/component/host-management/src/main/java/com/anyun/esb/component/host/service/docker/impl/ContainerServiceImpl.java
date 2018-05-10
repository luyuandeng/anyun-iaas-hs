package com.anyun.esb.component.host.service.docker.impl;

import com.anyun.cloud.api.Id;
import com.anyun.cloud.dto.*;
import com.anyun.cloud.param.*;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.json.JsonUtil;
import com.anyun.common.jbi.component.ServiceInvoker;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.anyun.esb.component.host.common.JdbcUtils;
import com.anyun.esb.component.host.common.SSHKeyVerifyHelper;
import com.anyun.esb.component.host.common.ScpClient;
import com.anyun.esb.component.host.dao.ContainerDao;
import com.anyun.esb.component.host.dao.HostBaseInfoDao;
import com.anyun.esb.component.host.dao.ProjectDao;
import com.anyun.esb.component.host.dao.impl.ContainerDaoImpl;
import com.anyun.esb.component.host.dao.impl.HostBaseInfoDaoImpl;
import com.anyun.esb.component.host.dao.impl.ProjectDaoImpl;
import com.anyun.esb.component.host.service.ContainerBasicService;
import com.anyun.esb.component.host.service.docker.*;
import com.anyun.exception.JbiComponentException;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.core.command.PullImageResultCallback;
import com.sun.glass.ui.View;
import org.hashids.Hashids;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gaopeng on 16-6-7.
 */
public class ContainerServiceImpl implements ContainerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ContainerServiceImpl.class);
    private ContainerDao containerDao = new ContainerDaoImpl();
    private HostBaseInfoDao hostBaseInfoDao = new HostBaseInfoDaoImpl();
    private DockerHostService dockerHostService = new DockerHostServiceImpl();
    private ApplicationService applicationService = new ApplicationServiceImpl();
    private TagService tagService = new TagServiceImpl();

    @Override
    public List<ContainerDto> queryAllContainerByHost(String host) throws Exception {
        List<ContainerDto> list = containerDao.selectAllContainerByHost(host);
        if (list == null || list.isEmpty())
            return null;
        for (int i = list.size() - 1; i >= 0; i--) {
            if (!queryContainerWhetherExists(list.get(i).getId()))
                list.remove(i);
        }
        return list;
    }

    boolean queryContainerWhetherExists(String id) throws Exception {
        LOGGER.debug("container:[{}]", id);
        if (StringUtils.isEmpty(id))
            throw new Exception("id is empty");
        String host = queryContainerDtoById(id).getHostId();
        String managementIp = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
        LOGGER.debug("managementIp:[{}]", managementIp);
        if (StringUtils.isEmpty(managementIp))
            return false;
        //创建连接
        DockerClient client = Env.getDockerClient(managementIp);
        try {
            InspectContainerResponse inspectContainerResponse = client.inspectContainerCmd(id).exec();
            if (inspectContainerResponse == null || StringUtils.isEmpty(inspectContainerResponse.getId()))
                return false;
            LOGGER.debug("container exists");
            LOGGER.debug("response id:[{}] ,status:[{}]", inspectContainerResponse.getId(), inspectContainerResponse.getState().getStatus());
            return true;
        } catch (Exception e) {
            LOGGER.debug("Container:[{}] does not exists :[{}]", id, e.getMessage());
            return false;
        } finally {
            //关闭连接
            client.close();
        }
    }


    @Override
    public ContainerDto createDefaultContainer(ContainerCreateByConditionParam param) throws Exception {
        //非空校验
        if (StringUtils.isEmpty(param.getProjectId()))
            throw new Exception("param.getProjectId() is empty");
        if (StringUtils.isEmpty(param.getImageId()))
            throw new Exception("param.getImageId() is empty");
        if (StringUtils.isEmpty(param.getHost()))
            throw new Exception("param.getHost() is empty");
        if (StringUtils.isEmpty(param.getIp()))
            throw new Exception("param.getHostIp is empty");

        //定义变量
        ProjectDao pDao = new ProjectDaoImpl();
        ProjectDto pDto = pDao.selectProjectById(param.getProjectId());

        if (pDto == null)
            throw new Exception("项目:[{" + param.getProjectId() + "]}不存在");

        //去除zookpeer 中没有的 host
        List<String> actionHost = dockerHostService.findAllActiveHosts();
        LOGGER.debug("Find action host:[{}]", actionHost);
        if (actionHost == null || actionHost.isEmpty() || actionHost.size() == 0)
            throw new Exception("Does not  find  action host");
        if (!actionHost.contains(param.getHost() + ":" + param.getIp()))
            throw new Exception("This host does not action");

        DockerImageDto dockerImageDto = containerDao.selectRegistImageInfoById(param.getImageId()); //查询已注册镜像信息
        if (dockerImageDto == null) {
            LOGGER.debug("该镜像[{}]未注册", param.getImageId());
            throw new Exception("该镜像未注册");
        }

        String url = "imagehub.anyuncloud.com/" + dockerImageDto.getCategory() + "/" + dockerImageDto.getName() + ":" + dockerImageDto.getTag();

        DockerClient client = null;  //创建docker连接
        client = Env.getDockerClient(param.getIp()); //查询宿主机是否存在该镜像
        LOGGER.debug("Client [{}]", client);
        InspectImageResponse inspectImageResponse = null;
        try {
            inspectImageResponse = client.inspectImageCmd(url).exec();
        } catch (Exception e) {
            LOGGER.debug("宿主机器[{}]：没有镜像：[{}]", param.getHost(), param.getImageId());
            if (inspectImageResponse == null) {
                LOGGER.debug("url:{}", url);
                LOGGER.debug("pull  images .............");
                PullImageResultCallback callback = new PullImageResultCallback();
                client.pullImageCmd(url).exec(callback);
                callback.awaitSuccess();
                LOGGER.debug("this:[{}]  imageId:[{}] pull success！", param.getIp(), param.getImageId());
            }
        }

        //生成短id
        Hashids hashids = new Hashids(StringUtils.uuidGen());
        String softId = hashids.encode(new Date().getTime());
        CreateContainerCmd cmd = client.createContainerCmd(url);
        cmd.withAttachStdin(true);
        cmd.withAttachStdout(true);
        cmd.withStdinOpen(true);
        cmd.withName(softId);
        cmd.withImage(url);
        List<Capability> capabilityList = new ArrayList<>();
        for (String string : param.getCapAdd().keySet()) {
            for (Capability capability : Capability.values()) {
                if (capability.name().equals(string)) {
                    capabilityList.add(capability);
                    break;
                }
            }
        }
        if (!capabilityList.isEmpty())
            cmd.withCapAdd(capabilityList);

        if (param.getPrivileged() == 1) {
            cmd.withPrivileged(true);
        }

        CreateContainerResponse response = cmd.exec();
        client.close();//关闭连接

        ContainerDto dto = new ContainerDto();
        Date currentDate = new Date();
        dto.setId(response.getId());
        dto.setShortId(softId);
        dto.setName(param.getName());
        dto.setHostName(param.getHostName());
        dto.setPrivileged(param.getPrivileged());
        dto.setHostId(param.getHost());
        dto.setImageId(param.getImageId());
        dto.setProjectId(param.getProjectId());
        dto.setCpuCoreLimit(param.getCpuCoreLimit());
        dto.setCpuFamily(param.getCpuFamily());
        dto.setMemoryLimit(param.getMemoryLimit());
        dto.setMemorySwapLimit(param.getMemorySwapLimit());
        dto.setStatus(1);
        dto.setType(0);
        dto.setPurpose(param.getPurpose());
        dto.setCreateTime(currentDate);
        dto.setLastModifyTime(currentDate);
        dto.setImageName(dockerImageDto.getName());
        dto.setCalculationSchemeId(param.getCalculationSchemeId());
        dto.setDiskSchemeId(param.getDiskSchemeId());
        //保存容器信息至数据库
        ContainerDto c = containerDao.insertContainer(dto);

        //连上项目网络标签
        NetService netService = new NetServiceImpl();
        NetLabelInfoDto netLabelInfoDto = netService.netLabelInfoQueryProject(param.getProjectId());
        ConnectToNetParam p = new ConnectToNetParam();
        p.setContainer(response.getId());
        p.setLabel(netLabelInfoDto.getLabel());
        netService.containerConnectToNetwork(p);
        try {
            //从容器中移除 docker0 网卡（默认网卡）
            VSwitchService vSwitchService = new VSwitchServiceImpl();
            vSwitchService.disconnectFromNetwork(response.getId(), "bridge", "bridge");
            LOGGER.debug("disconnectFromNetwork  bridge success");
        } catch (Exception e) {
            LOGGER.debug("disconnectFromNetwork  bridge fail :[{}]", e.getMessage());
        }

        try {
            //变更磁盘方案
            ContainerChangeDiskSchemeParam containerChangeDiskSchemeParam = new ContainerChangeDiskSchemeParam();
            containerChangeDiskSchemeParam.setDiskSchemeId(param.getDiskSchemeId());
            containerChangeDiskSchemeParam.setId(response.getId());
            changeDiskScheme(containerChangeDiskSchemeParam);
        } catch (Exception e) {
            LOGGER.debug("容器磁盘方案变更失败！");
        }

        try {
            //变更计算方案
            ContainerChangeCalculationSchemeParam containerChangeCalculationSchemeParam = new ContainerChangeCalculationSchemeParam();
            containerChangeCalculationSchemeParam.setId(response.getId());
            containerChangeCalculationSchemeParam.setCalculationSchemeId(param.getCalculationSchemeId());
            changeCalculationScheme(containerChangeCalculationSchemeParam);
        } catch (Exception e) {
            LOGGER.debug("容器计算方案变更失败！");
        }

        return c;
    }

    @Override
    public void startContainer(String id) throws Exception {
        String host = queryContainerDtoById(id).getHostId();
        String ip = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
        if (StringUtils.isEmpty(ip)) {
            LOGGER.debug("management ip is empty");
            throw new Exception("management ip is empty");
        }
        LOGGER.debug("management ip  is: {}", ip);
        DockerClient client = Env.getDockerClient(ip);
        InspectContainerResponse inspectContainerResponse;
        try {
            inspectContainerResponse = client.inspectContainerCmd(id).exec();
        } catch (Exception e) {
            LOGGER.warn("Exception  is:", e);

            throw new Exception("容器[" + id + "]不存在");
        }
        if (inspectContainerResponse != null) {
            String status = inspectContainerResponse.getState().getStatus();
            LOGGER.debug("begin------container  status is: {}", status);
            //判断容器状态
            switch (status) {
                case "running":
                    break;
                case "created":
                    client.startContainerCmd(id).exec();
                    break;
                case "paused":
                    client.unpauseContainerCmd(id).exec();
                    break;
                case "exited":
                    client.startContainerCmd(id).exec();
                    break;
                default:
                    throw new Exception("容器[" + id + "] 状态未知");
            }
            LOGGER.debug("finish------container  status is: {}", client.inspectContainerCmd(id).exec().getState().getStatus());
        }
        ContainerDto dto = new ContainerDto();
        Date date = new Date();
        dto.setId(id);
        dto.setStatus(2);
        dto.setLastModifyTime(date);
        containerDao.updateContainer(dto);
    }

    @Override
    public void stopContainer(String id) throws Exception {
        String host = queryContainerDtoById(id).getHostId();
        String ip = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
        if (StringUtils.isEmpty(ip)) {
            LOGGER.debug("management ip is empty");
            throw new Exception("management ip is empty");
        }
        LOGGER.debug("management ip is{}", ip);
        DockerClient client = Env.getDockerClient(ip);
        InspectContainerResponse inspectContainerResponse;
        try {
            inspectContainerResponse = client.inspectContainerCmd(id).exec();
        } catch (Exception e) {
            LOGGER.debug("Exception  is:{}", e.getMessage());
            throw new Exception("容器[" + id + "]不存在");
        }
        if (inspectContainerResponse != null) {
            String status = inspectContainerResponse.getState().getStatus();
            LOGGER.debug("begin------container  status is: {}", status);
            //判断容器状态
            switch (status) {
                case "created":
                    break;
                case "exited":
                    break;
                case "running":
                    client.stopContainerCmd(id).exec();
                    break;
                case "paused":
                    client.unpauseContainerCmd(id).exec();
                    client.stopContainerCmd(id).exec();
                    break;
                default:
                    throw new Exception("容器[" + id + "] 状态未知");
            }
            LOGGER.debug("finish------container  status is: {}", client.inspectContainerCmd(id).exec().getState().getStatus());
        }
        ContainerDto dto = new ContainerDto();
        Date date = new Date();
        dto.setId(id);
        dto.setStatus(4);
        dto.setLastModifyTime(date);
        containerDao.updateContainer(dto);
    }

    @Override
    public void deleteContainer(String id) throws Exception {
        LOGGER.debug("id:[{}]", id);
        if (StringUtils.isEmpty(id))
            throw new Exception("id is empty");
        String host = queryContainerDtoById(id).getHostId();
        //查询管理ip
        String ip = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
        LOGGER.debug("managementIp:[{}]", ip);
        if (StringUtils.isEmpty(ip))
            throw new Exception("management ip is empty");
        DockerClient client = Env.getDockerClient(ip);
        InspectContainerResponse inspectContainerResponse;
        try {
            inspectContainerResponse = client.inspectContainerCmd(id).exec();
        } catch (Exception e) {
            throw new Exception("容器[" + id + "]不存在");
        }

        //删除应用
        applicationService.applicationDeleteByContainer(id);

        //解除容器与网络标签关联
        NetService netService = new NetServiceImpl();
        List<NetLabelInfoDto> list = netService.netLabelInfoQueryByContainer(id);
        if (list != null && !list.isEmpty()) {
            LOGGER.debug("size:[{}]", list.size());
            for (NetLabelInfoDto n : list) {
                DisconnectFromNetParam disconnectFromNetParam = new DisconnectFromNetParam();
                disconnectFromNetParam.setLabel(n.getLabel());
                disconnectFromNetParam.setContainer(id);
                netService.containerDisconnectFromNetwork(disconnectFromNetParam);
            }
        }


        //删除容器
        if (inspectContainerResponse != null) {
            String status = inspectContainerResponse.getState().getStatus();
            LOGGER.debug("begin------container  status is: {}", status);
            //判断容器状态
            switch (status) {
                case "created":
                    client.removeContainerCmd(id).exec();
                    break;
                case "exited":
                    client.removeContainerCmd(id).exec();
                    break;
                case "running":
                    client.removeContainerCmd(id).withForce(true).exec();
                    break;
                case "paused":
                    client.unpauseContainerCmd(id).exec();
                    client.removeContainerCmd(id).withForce(true).exec();
                    break;
                default:
                    throw new Exception("容器[" + id + "] 状态未知");
            }
        }

        // 刪除資源關聯表
        tagService.tagDeleteByResourceId(id);
        containerDao.deleteContainerById(id);
    }

    @Override
    public void deleteContainerByProject(String project) throws Exception {
        List<ContainerDto> list = queryContainerByProject(project, 0);
        if (list != null && !list.isEmpty()) {
            for (ContainerDto c : list) {
                //删除容器
                deleteContainer(c.getId());
                LOGGER.debug("容器: [{}] 删除成功 ", c.getId());
            }
        }
    }

    @Override
    public ContainerDto queryContainerById(String id) throws Exception {
        ContainerDto containerDto = containerDao.selectContainerById(id);
        if (containerDto == null || StringUtils.isEmpty(containerDto.getId())) {
            return null;
        }
        if (!queryContainerWhetherExists(id))
            return null;
        return containerDto;
    }

    @Override
    public ContainerDto queryContainerById(String id, int type) throws Exception {
        ContainerDto containerDto = containerDao.selectContainerById(id, type);
        if (containerDto == null || StringUtils.isEmpty(containerDto.getId())) {
            return null;
        }
        if (!queryContainerWhetherExists(id))
            return null;
        return containerDto;
    }

    @Override
    public List<ContainerDto> queryContainerByProject(String project, int type) throws Exception {
        List<ContainerDto> list = containerDao.selectContainerByProject(project, type);
        if (list == null)
            return null;
        List<ContainerDto> l = new ArrayList<>();
        for (ContainerDto containerDto : list) {
            if (containerDto == null || StringUtils.isEmpty(containerDto.getId()))
                continue;
            if (queryContainerWhetherExists(containerDto.getId()))
                l.add(containerDto);
        }
        return l;
    }

    @Override
    public List<ContainerDto> queryContainerByNetLabel(String label, int type) throws Exception {
        List<ContainerDto> list = containerDao.selectContainerByNetLabel(label, type);
        if (list != null && !list.isEmpty()) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (!queryContainerWhetherExists(list.get(i).getId()))
                    list.remove(i);
            }
        }
        return list;
    }

    @Override
    public List<ContainerDto> queryContainerByNetLabel(String label) throws Exception {
        List<ContainerDto> list = containerDao.selectContainerByLabel(label);
        if (list != null && !list.isEmpty()) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (!queryContainerWhetherExists(list.get(i).getId()))
                    list.remove(i);
            }
        }
        return list;
    }

    @Override
    public List<ContainerDto> queryContainerToBePublished(String project, String purpose, int type) throws Exception {
        List<ContainerDto> list = containerDao.selectContainerByPurpose(project, purpose, type);
        if (list == null)
            return null;
        List<ApplicationInfoDto> l = applicationService.applicationQueryByProject(project);
        if (l == null)
            return list;
        for (int i = list.size() - 1; i >= 0; i--) {
            for (ApplicationInfoDto a : l) {
                if (a.getTemplateContainer().equals(list.get(i).getId()))
                    list.remove(i);
            }
        }
        System.out.print(list.size());
        return list;
    }

    @Override
    public List<ContainerDto> queryContainerByImage(String image, int type) throws Exception {
        List<ContainerDto> list = containerDao.selectContainerByImage(image, type);
        if (list == null || list.isEmpty())
            return null;
        List<ContainerDto> l = new ArrayList<>();
        for (ContainerDto containerDto : list) {
            if (containerDto == null || StringUtils.isEmpty(containerDto.getId()))
                continue;
            if (queryContainerWhetherExists(containerDto.getId()))
                l.add(containerDto);
        }
        return l;
    }

    @Override
    public ContainerIpInfoDto createLoadContainer(String host, String managementIp, String templateMirror, ContainerDto containerDto) throws Exception {
        //定义负载Dto
        ContainerDto c = containerDto;
        String project = c.getProjectId();

        //创建连接
        DockerClient client = Env.getDockerClient(managementIp);

        //创建容器
        Hashids hashids = new Hashids(StringUtils.uuidGen());
        String softId = hashids.encode(new Date().getTime());
        CreateContainerCmd createContainerCmd = client.createContainerCmd(templateMirror);
        createContainerCmd.withAttachStdin(true);
        createContainerCmd.withAttachStdout(true);
        createContainerCmd.withStdinOpen(true);
        createContainerCmd.withName(softId);
        CreateContainerResponse response = createContainerCmd.exec();
        if (response == null)
            throw new Exception("LoadContainer create fail");
        String id = response.getId();

        //保存 容器
        c.setId(id);
        c.setShortId(softId);
        c.setType(2);
        c.setPurpose("LOAD");
        containerDao.insertContainer(c);

        //连上 openvswitch
        NetService netService = new NetServiceImpl();
        NetLabelInfoDto netLabelInfoDto = netService.netLabelInfoQueryProject(project);
        ConnectToNetParam connect = new ConnectToNetParam();
        connect.setContainer(id);
        connect.setLabel(netLabelInfoDto.getLabel());
        netService.containerConnectToNetwork(connect);
        try {
            //从容器中移除  bridge网卡（默认网卡）
            VSwitchService vSwitchService = new VSwitchServiceImpl();
            vSwitchService.disconnectFromNetwork(id, "bridge", "bridge");
            LOGGER.debug("disconnectFromNetwork  bridge success");
        } catch (Exception e) {
            LOGGER.debug("disconnectFromNetwork  bridge fail :[{}]", e.getMessage());
        }

        //关闭client
        if (client != null)
            client.close();
        return netService.queryContainerIpInfoByCondition(id, netLabelInfoDto.getLabel());
    }

    @Override
    public String createNginxContainer(String host, String managementIp, String nginxConfig, String application, String bridge, String ip, int port, ContainerDto templateContainerDto, String privateKey, String certificate) throws Exception {
        ContainerDto containerDto = templateContainerDto;
        String project = containerDto.getProjectId();
        HostSshClient hostSshClient = Env.getSshClient(host + ":" + managementIp);
        if (hostSshClient == null)
            throw new Exception("hostSshClient is empty");
        if (port == 443) {
            String confDir = "/apps/" + project + "/nginx/" + application + "/etc";
            String centDir = "/apps/" + project + "/nginx/" + application + "/cert";

            String conf = confDir + "/nginx.conf";
            String cert = centDir + "/server.cert";
            String key = centDir + "/server.key";

            //创建文件夹
            String createFolderCommand = "mkdir -p" + " " + confDir + " " + centDir;
            hostSshClient.exec(createFolderCommand);

            //创建文件
            String createFileCommand = "touch" + " " + conf + " " + cert + " " + key;
            hostSshClient.exec(createFileCommand);

            //文件添 权限
            String authorizationFile = "chmod a+rw" + " " + conf + " " + cert + " " + key;
            hostSshClient.exec(authorizationFile);

            //写入  配置
            File configFile = File.createTempFile("nginx", ".conf");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
            bufferedWriter.write(nginxConfig);
            bufferedWriter.close();
            //copy file
            ScpClient scpClient = new ScpClient(hostSshClient.getSession());
            scpClient.scp(configFile, conf);
            try {//删除临时文件
                if (configFile.exists())
                    configFile.delete();
            } catch (Exception d) {
                LOGGER.debug("configFile delete  fail:[{}]", d);
            }
            hostSshClient.exec("echo" + " " + certificate + " " + ">" + " " + cert);
            hostSshClient.exec("echo" + " " + privateKey + " " + ">" + " " + key);

            //创建NG  并挂载配置
            DockerClient client = Env.getDockerClient(managementIp);//创建连接
            Hashids hashids = new Hashids(StringUtils.uuidGen());
            String softId = hashids.encode(new Date().getTime());
            String url = "imagehub.anyuncloud.com/middleware/nginx:1.0.0";//NG 镜像

            InspectImageResponse inspectImageResponse = null;
            try {
                inspectImageResponse = client.inspectImageCmd(url).exec();
            } catch (Exception e) {
                LOGGER.debug("Start   pull  images ..............................");
                PullImageResultCallback callback = new PullImageResultCallback();
                client.pullImageCmd(url).exec(callback);
                callback.awaitSuccess();
                LOGGER.debug("nginxImage:[{}] pull success！", url);
            }

            String create = "docker  run  --name" + " " + softId + " " + "-i  -d  -v" + " " + confDir + ":/etc/nginx/conf.d   -v" + " " + centDir + ":/etc/nginx/cert  imagehub.anyuncloud.com/middleware/nginx:1.0.0   /bin/bash";
            String response = hostSshClient.exec(create);
            LOGGER.debug("response:[{}]" + response);
            containerDto.setId(response);
            containerDto.setShortId(softId);
            containerDto.setType(1);
            containerDto.setPurpose("NGINX");
            containerDto.setHostId(host);
            containerDao.insertContainer(containerDto);

            //连上 openvswitch
            NetService netService = new NetServiceImpl();
            NetLabelInfoDto netLabelInfoDto = netService.netLabelInfoQueryProject(project);
            ConnectToNetParam p = new ConnectToNetParam();
            p.setContainer(response);
            p.setLabel(netLabelInfoDto.getLabel());
            netService.containerConnectToNetwork(p);

            try {
                //从容器中移除 bridge网卡（默认网卡）
                VSwitchService vSwitchService = new VSwitchServiceImpl();
                vSwitchService.disconnectFromNetwork(response, "bridge", "bridge");
                LOGGER.debug("disconnectFromNetwork  bridge success");
            } catch (Exception e) {
                LOGGER.debug("disconnectFromNetwork  bridge fail :[{}]", e.getMessage());
            }

            //连上 bridge
            ConnectToNetParam c2 = new ConnectToNetParam();
            c2.setContainer(response);
            c2.setLabel(bridge);
            c2.setIp(ip);
            netService.containerConnectToNetwork(c2);

            //启动nginx  服务
            try {
                //重启 容器
                hostSshClient.exec("docker restart" + " " + response);
                // /etc/init.d/nginx restart
                // service  nginx  restart
                String command = "docker exec -d" + " " + response + " " + "/etc/init.d/nginx restart";
                LOGGER.debug("restartNginxService command:[{}]", command);
                hostSshClient.exec(command);
                return response;
            } catch (Exception r) {
                LOGGER.debug("Nginx restart fail:[{}]", r);
                return response;
            }
        } else {
            String confDir = "/apps/" + project + "/nginx/" + application + "/etc";
            String conf = confDir + "/nginx.conf";

            //创建文件夹
            String createFolderCommand = "mkdir -p" + " " + confDir;
            hostSshClient.exec(createFolderCommand);

            //创建文件
            String createFileCommand = "touch" + " " + conf;
            hostSshClient.exec(createFileCommand);

            //文件添 权限
            String authorizationFile = "chmod a+rw" + " " + conf;
            hostSshClient.exec(authorizationFile);

            //写入  配置
            File configFile = File.createTempFile("nginx", ".conf");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(configFile));
            bufferedWriter.write(nginxConfig);
            bufferedWriter.close();
            //copy file
            ScpClient scpClient = new ScpClient(hostSshClient.getSession());
            scpClient.scp(configFile, conf);
            try {//删除临时文件
                if (configFile.exists())
                    configFile.delete();
            } catch (Exception d) {
                LOGGER.debug("configFile delete  fail:[{}]", d);
            }

            //创建NG  并挂载配置
            DockerClient client = Env.getDockerClient(managementIp);//创建连接
            Hashids hashids = new Hashids(StringUtils.uuidGen());
            String softId = hashids.encode(new Date().getTime());
            String url = "imagehub.anyuncloud.com/middleware/nginx:1.0.0";//NG 镜像

            InspectImageResponse inspectImageResponse = null;
            try {
                inspectImageResponse = client.inspectImageCmd(url).exec();
            } catch (Exception e) {
                LOGGER.debug("Start   pull  images ..............................");
                PullImageResultCallback callback = new PullImageResultCallback();
                client.pullImageCmd(url).exec(callback);
                callback.awaitSuccess();
                LOGGER.debug("nginxImage:[{}] pull success！", url);
            }

            String create = "docker  run  --name" + " " + softId + " " + "-i  -d  -v" + " " + confDir + ":/etc/nginx/conf.d  imagehub.anyuncloud.com/middleware/nginx:1.0.0   /bin/bash";
            String response = hostSshClient.exec(create);
            LOGGER.debug("response:[{}]" + response);
            containerDto.setId(response);
            containerDto.setShortId(softId);
            containerDto.setType(1);
            containerDto.setPurpose("NGINX");
            containerDto.setHostId(host);
            containerDao.insertContainer(containerDto);

            //连上 openvswitch
            NetService netService = new NetServiceImpl();
            NetLabelInfoDto netLabelInfoDto = netService.netLabelInfoQueryProject(project);
            ConnectToNetParam p = new ConnectToNetParam();
            p.setContainer(response);
            p.setLabel(netLabelInfoDto.getLabel());
            netService.containerConnectToNetwork(p);

            try {
                //从容器中移除 bridge网卡（默认网卡）
                VSwitchService vSwitchService = new VSwitchServiceImpl();
                vSwitchService.disconnectFromNetwork(response, "bridge", "bridge");
                LOGGER.debug("disconnectFromNetwork  bridge success");
            } catch (Exception e) {
                LOGGER.debug("disconnectFromNetwork  bridge fail :[{}]", e.getMessage());
            }

            //连上 bridge
            ConnectToNetParam c2 = new ConnectToNetParam();
            c2.setContainer(response);
            c2.setLabel(bridge);
            c2.setIp(ip);
            netService.containerConnectToNetwork(c2);

            //启动nginx  服务
            try {
                //重启 容器
                hostSshClient.exec("docker restart" + " " + response);
                // /etc/init.d/nginx restart
                // service  nginx  restart
                String command = "docker exec -d " + " " + response + " " + "/etc/init.d/nginx restart";
                LOGGER.debug("restartNginxService command:[{}]", command);
                hostSshClient.exec(command);
                return response;
            } catch (Exception r) {
                LOGGER.debug("Nginx restart fail:[{}]", r);
                return response;
            }

        }
    }

    @Override
    public void stopContainer(String id, String type) throws Exception {
        //判斷容器是否存在
        int Type = Integer.parseInt(type);
        ContainerDto containerDto = queryContainerById(id, Type);
        if (containerDto == null) {
            throw new Exception("container is null");
        }
        String ip = hostBaseInfoDao.selectManagementIpDtoByHost(id).getIpAddress();
        if (StringUtils.isEmpty(ip)) {
            LOGGER.debug("management ip is empty");
            throw new Exception("management ip is empty");
        }
        LOGGER.debug("macagement ip is{}", ip);
        DockerClient client = Env.getDockerClient(ip);
        InspectContainerResponse inspectContainerResponse;
        try {
            inspectContainerResponse = client.inspectContainerCmd(id).exec();
        } catch (Exception e) {
            LOGGER.debug("Exception  is:{}", e.getMessage());
            throw new Exception("容器[" + id + "]不存在");
        }
        if (inspectContainerResponse != null) {
            String status = inspectContainerResponse.getState().getStatus();
            LOGGER.debug("begin------container  status is: {}", status);
            //判斷容器狀態
            switch (status) {
                case "created":
                    break;
                case "exited":
                    break;
                case "running":
                    client.stopContainerCmd(id).exec();
                    break;
                case "paused":
                    client.unpauseContainerCmd(id).exec();
                    client.stopContainerCmd(id).exec();
                    break;
                default:
                    throw new Exception("容器[" + id + "]状态未知");
            }
            LOGGER.debug("finish-------container  status is: {}", client.inspectContainerCmd(id).exec().getState().getStatus());
        }
        //更新數據庫狀態
        Date date = new Date();
        containerDto.setId(id);
        containerDto.setStatus(4);
        containerDto.setLastModifyTime(date);
        containerDao.updateContainer(containerDto);
    }


    //宿主机运行时间
    public SystemUptimeDto getHostSystemUptime(String ip) throws Exception {
        LOGGER.debug("ip[{}]", ip);
        String host = hostBaseInfoDao.selectHostIdByIp(ip);
        String managementIp = hostBaseInfoDao.selectById(host).getHostManagementIpInfoDto().getIpAddress();
        HostSshClient hostSshClient = Env.getSshClient(ip + ":" + managementIp);
        if (hostSshClient == null)
            throw new Exception("hostSshClient is empty");
        ;
        String alreadyRunTime = hostSshClient.exec("cat /proc/uptime").split(" ")[0].trim();
        SystemUptimeDto systemUptimeDto = new SystemUptimeDto();
        systemUptimeDto.setAlreadyRunTime(alreadyRunTime);

        String startTime = hostSshClient.exec("date -d \"$(awk -F. '{print $1}' /proc/uptime) second ago\" +\"%Y-%m-%d %H:%M:%S\"  ").trim();
        systemUptimeDto.setStartTime(startTime);

        String f = "cat /proc/uptime | awk -F. '{run_days=$1 / 86400;run_hour=($1 % 86400)/3600;run_minute=($1 % 3600)/60;run_second=$1 % 60;printf(\"%d %d %d %d\\n\",run_days,run_hour,run_minute,run_second)}'";
        String formatTimes = hostSshClient.exec(f).trim();
        systemUptimeDto.setFormatTime(formatTimes);
        return systemUptimeDto;
    }


    //容器运行时间
    public SystemUptimeDto getContainerSystemUptime(String container) throws Exception {
        LOGGER.debug("container[{}]", container);
        ContainerDto containerDto = queryContainerById(container);
        String host = containerDto.getHostId();
        HostManagementIpInfoDto hostManagementIpInfoDto = hostBaseInfoDao.selectManagementIpDtoByHost(containerDto.getHostId());
        String managementIp = hostManagementIpInfoDto.getIpAddress();
        HostSshClient hostSshClient = Env.getSshClient(host + ":" + managementIp);
        if (hostSshClient == null)
            throw new Exception("hostSshClient is empty");
        SystemUptimeDto systemUptimeDto = new SystemUptimeDto();
        String response = hostSshClient.exec("docker ps|grep " + " " + containerDto.getShortId());
        String formatTime = "";
        if (StringUtils.isNotEmpty(response)) {
            String s1 = response.split("Up")[1].trim().split(" ")[0];
            String s2 = response.split("Up")[1].trim().split(" ")[1];
            String s3 = response.split("Up")[1].trim().split(" ")[2];
            if (s1.trim().equals("About"))
                formatTime = s1 + " " + s2 + " " + s3;
            else
                formatTime = s1 + " " + s2;
        }
        systemUptimeDto.setFormatTime(formatTime);
        return systemUptimeDto;
    }


    @Override
    public List<ContainerDto> containerQuery(String userUniqueId, String subMethod, String subParameters) throws Exception {
        List<ContainerDto> l = null;
        switch (subMethod) {
            case "QUERY_BY_CONTAINER":
                l = QUERY_BY_CONTAINER(userUniqueId, subParameters);
                break;

            case "QUERY_BY_IMAGE":
                l = QUERY_BY_IMAGE(userUniqueId, subParameters);
                break;

            case "QUERY_BY_VOLUME":
                l = QUERY_BY_VOLUME(userUniqueId, subParameters);
                break;

            case "QUERY_BY_LABEL":
                l = QUERY_BY_LABEL(userUniqueId, subParameters);
                break;
            case "QUERY_NOT_CONNECTED_BY_LABEL":
                l = QUERY_NOT_CONNECTED_BY_LABEL(subParameters);
                break;

            case "QUERY_BY_PROJECT":
                l = QUERY_BY_PROJECT(userUniqueId, subParameters);
                break;

            case "QUERY_UNMOUNTED_BY_PROJECT_VOLUME":
                String[] array = subParameters.replace("|", ",").split(",");
                if (array == null || array.equals("") || array.length != 2)
                    throw new Exception("subParameters format error");
                String project = array[0];
                String volume = array[1];
                l = QUERY_UNMOUNTED_BY_PROJECT_VOLUME(userUniqueId, project, volume);
                break;

            case "QUERY_UNPUBLISHED_BY_PROJECT":
                l = QUERY_UNPUBLISHED_BY_PROJECT(userUniqueId, subParameters);
                break;

            default:
                throw new Exception("subMethod:[" + subMethod + "]  does not exist");
        }
        return l;
    }


    @Override
    public ContainerOnHostDto getContainerOnHostDtoByHost(String host) throws Exception {
        List<ContainerDto> list = queryAllContainerByHost(host);
        ContainerOnHostDto containerOnHostDto = null;
        if (list != null && !list.isEmpty()) {
            containerOnHostDto = new ContainerOnHostDto();
            containerOnHostDto.setList(list);
            containerOnHostDto.setTotal(list.size());
            int run = 0;
            for (ContainerDto c : list) {
                if (StringUtils.isEmpty(c.getId()))
                    continue;
                if (c.getStatus() == 2)
                    run++;
            }
            containerOnHostDto.setRun(run);
            containerOnHostDto.setStop(list.size() - run);
        }

        return containerOnHostDto;
    }

    private static List<ContainerDto> QUERY_BY_CONTAINER(String userUniqueId, String container) throws Exception {
        ServiceInvoker<Map> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("container_query_by_id");
        Map<String, String> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("id", container);
        Map response = invoker.invoke(param, null);
        ContainerDto containerDto = JsonUtil.fromJson(ContainerDto.class, JsonUtil.toJson(response));
        List<ContainerDto> list = null;
        if (containerDto != null && StringUtils.isNotEmpty(containerDto.getId())) {
            list = new ArrayList<>();
            list.add(containerDto);
        }
        return list;
    }


    private static List<ContainerDto> QUERY_BY_IMAGE(String userUniqueId, String image) throws Exception {
        ServiceInvoker<List> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("container_query_by_image");
        Map<String, String> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("id", image);
        List response = invoker.invoke(param, null);
        List<ContainerDto> list = JsonUtil.fromJson(List.class, JsonUtil.toJson(response));
        return list;
    }


    private static List<ContainerDto> QUERY_BY_VOLUME(String userUniqueId, String volume) throws Exception {
        ServiceInvoker<List> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("container_query_by_volume");
        Map<String, String> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("id", volume);
        List response = invoker.invoke(param, null);
        List<ContainerDto> list = JsonUtil.fromJson(List.class, JsonUtil.toJson(response));
        return list;
    }


    private static List<ContainerDto> QUERY_BY_LABEL(String userUniqueId, String label) throws Exception {
        ServiceInvoker<List> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("container_query_by_netLabel");
        Map<String, String> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("id", label);
        List response = invoker.invoke(param, null);
        List<ContainerDto> list = JsonUtil.fromJson(List.class, JsonUtil.toJson(response));
        return list;
    }


    private List<ContainerDto> QUERY_NOT_CONNECTED_BY_LABEL(String label) throws Exception {
        if (StringUtils.isEmpty(label))
            throw new Exception("The label  empty");
        NetService netS = new NetServiceImpl();
        NetLabelInfoDto netDto = netS.netLabelInfoQueryByLabel(label);//获取该标签的Dto
        LOGGER.debug("The label dto is :[{}]", netDto.asJson());
        if (netDto == null)
            throw new Exception("The label does not exist");
        String type = netDto.getType();  //标签类型
        LOGGER.debug("The label type:[{}]", type);
        List<ContainerDto> containerDtoList = null;
        Iterator<ContainerDto> it = null;
        if (type.equals("openvswitch")) { //ovs 类型为 openvswitch
            ProjectService projectS = new ProjectServiceImpl();
            ProjectDto projectDto = projectS.queryProjectDtoByPlatFormNetworkId(label);//查询项目Dto 由 openvswitch 标签
            if (projectDto == null)
                throw new Exception("The projectDto is empty");
            String projectId = projectDto.getId();//项目Id
            containerDtoList = queryContainerByProject(projectId, 0);
            if (containerDtoList == null) {//项目下没有用户容器
                return null;
            }
            it = containerDtoList.iterator();
            while (it.hasNext()) {
                ContainerDto containerDto = it.next();
                ContainerIpInfoDto containerIpInfoDto = netS.queryContainerIpDtoByContainerAndNetLabel(containerDto.getId(), label);
                if (containerIpInfoDto != null)
                    it.remove();
            }
        } else {//ovs 类型为 bridge
            containerDtoList = queryContainerByType(0);
            if (containerDtoList == null)
                return null;
            it = containerDtoList.iterator();
            while (it.hasNext()) {
                ContainerDto containerDto = it.next();
                ContainerIpInfoDto containerIpInfoDto = netS.queryContainerIpDtoByContainerAndNetLabel(containerDto.getId(), label);
                if (containerIpInfoDto != null) {
                    it.remove();
                }
            }
        }
        return containerDtoList;
    }


    public List<ContainerDto> queryContainerByType(int type) throws Exception {
        return containerDao.selectContainerByType(type);
    }

    private static List<ContainerDto> QUERY_BY_PROJECT(String userUniqueId, String project) throws Exception {
        ServiceInvoker<List> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("container_query_by_Project");
        Map<String, String> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("id", project);
        List response = invoker.invoke(param, null);
        List<ContainerDto> list = JsonUtil.fromJson(List.class, JsonUtil.toJson(response));
        return list;
    }


    private static List<ContainerDto> QUERY_UNMOUNTED_BY_PROJECT_VOLUME(String userUniqueId, String project, String volume) throws Exception {
        ServiceInvoker<List> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("container_query_by_projectAndVolume");
        Map<String, String> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("project", project);
        param.put("volume", volume);
        List response = invoker.invoke(param, null);
        List<ContainerDto> list = JsonUtil.fromJson(List.class, JsonUtil.toJson(response));
        return list;
    }

    private static List<ContainerDto> QUERY_UNPUBLISHED_BY_PROJECT(String userUniqueId, String project) throws Exception {
        ServiceInvoker<List> invoker = new ServiceInvoker<>();
        LOGGER.debug("Service invoke camel context [{}]", invoker.getCamelContext());
        invoker.setComponent("anyun-host");
        invoker.setService("container_query_by_projectAndType");
        Map<String, String> param = new HashMap<>();
        param.put("userUniqueId", userUniqueId);
        param.put("project", project);
        param.put("type", "0");
        List response = invoker.invoke(param, null);
        List<ContainerDto> list = JsonUtil.fromJson(List.class, JsonUtil.toJson(response));
        return list;
    }


    @Override
    public PageDto<ContainerDto> containerlistquery(CommonQueryParam param) throws Exception {
        PageDto<ContainerDto> pageDto = new PageDto<>();//定义返回格式
        String selectStatement = "";//select 查询语句
        String str = JdbcUtils.ListToString(param.getConditions()); //String  类型条件
        LOGGER.debug("条件转String 结果:[{}]", str);
        if (str.contains("__userTag__")) { //where 条件包含 __userTag__ 相关
            selectStatement = "SELECT anyuncloud.container_info_base.* FROM  anyuncloud.container_info_base  left   join   anyuncloud.tag_info   on  anyuncloud.container_info_base.id  =  anyuncloud.tag_info.resourceId";
        } else {
            selectStatement = "SELECT * FROM anyuncloud.container_info_base";
        }
        LOGGER.debug("selectStatement:[{}]", selectStatement);
        String whereStatement = ("empty".equals(str) == true ? "" : "where" + str);//where 条件语句
        LOGGER.debug("whereStatement:[{}]", whereStatement);
        String sql = "";//sql 语句
        int total = 0;//总记录
        if (param.isCount()) {//返回总条数  不进行分页查询
            sql = selectStatement + " " + whereStatement;
            LOGGER.debug("sql:[{}]", sql);
            List<ContainerDto> list = JdbcUtils.getList(ContainerDto.class, sql);
            total = (list == null ? 0 : list.size());
            pageDto.setPageSize(param.getLimit());
            pageDto.setPageNumber(param.getStart());
            pageDto.setTotal(total);
            return pageDto;
        } else { //分页查询
            int start = param.getStart();//查询页码
            int limit = param.getLimit();//每页记录
            boolean replyWithCount = param.isReplyWithCount();//是否返回总条数
            String sortBy = param.getSortBy();//排序字段
            String sortDirection = param.getSortDirection();//排序规则

            // 分页参数校验
            if (start <= 0)
                throw new Exception("start is:{" + start + "}");
            if (limit <= 0)
                throw new Exception("limit is:{" + limit + "}");
            int i = (start - 1) * limit;
            String pagingStatement = "limit" + " " + i + "," + limit;  //分页 语句

            //排序 语句
            String sortingStatement = (sortBy != null && !"".equals(sortBy) ? "order by" + " " + sortBy + " " + sortDirection : "");

            sql = selectStatement + " " + whereStatement + " ";
            //查询全部记录
            List<ContainerDto> list = JdbcUtils.getList(ContainerDto.class, sql);
            if (list == null && list.size() == 0) {
                pageDto.setData(list);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = 0;
                //分页查询
            } else {
                sql = selectStatement + " " + whereStatement + " " + sortingStatement + " " + pagingStatement;
                LOGGER.debug("sql:[{}]", sql);
                List<ContainerDto> l = JdbcUtils.getList(ContainerDto.class, sql);
                pageDto.setData(l);
                pageDto.setPageNumber(start);
                pageDto.setPageSize(limit);
                total = list.size();
            }
            if (replyWithCount)
                pageDto.setTotal(total);
            return pageDto;
        }
    }

    @Override
    public List<String> selectContianerByHostId(String id) {
        List<String> containerList = containerDao.selectContianerByHostId(id);
        return containerList;
    }

    @Override
    public ContainerDto queryContainerDtoById(String id) throws Exception {
        return containerDao.selectContainerDtoById(id);
    }

    @Override
    public String createTemplateImage(String host, String managementIp, String application, String templateContainer) throws Exception {
        //创建连接
        DockerClient client = Env.getDockerClient(managementIp);
        String images = "apps/" + application + "/" + templateContainer + ":1.0";
        InspectImageResponse inspectImageResponse = null;
        try {
            inspectImageResponse = client.inspectImageCmd(images).exec();
        } catch (Exception e) {
            if (inspectImageResponse == null) {
                //创建模板镜像
                CommitCmd commitCmd = client.commitCmd(templateContainer).withRepository("apps/" + application + "/" + templateContainer).withTag("1.0");
                String str = commitCmd.exec();
                LOGGER.debug("response str:[{}]", str);
            }
        }
        return images;
    }

    @Override
    public List<ContainerDto> batchCreate(List<ContainerCreateByConditionParam> param) throws Exception {
        List<ContainerDto> list = new ArrayList<>();
        for (ContainerCreateByConditionParam p : param) {
            try {
                ContainerDto c = createDefaultContainer(p);
                list.add(c);
            } catch (Exception e) {
                LOGGER.debug("容器创建失败：[{}]", p.asJson());
            }
        }
        return list;
    }

    @Override
    public void changeDiskScheme(ContainerChangeDiskSchemeParam param) throws Exception {
        //参数校验
        if (param == null) {
            LOGGER.debug("Params is empty");
            return;
        }

        if (StringUtils.isEmpty(param.getId())) {
            LOGGER.debug("Container id is empty");
            return;
        }

        ContainerDto containerDto = containerDao.selectContainerDtoById(param.getId());
        if (containerDto == null) {
            LOGGER.debug("ContainerDto is empty");
            return;
        }

        if (StringUtils.isEmpty(param.getDiskSchemeId())) {
            LOGGER.debug("DiskScheme id is empty");
            return;
        }

        HostManagementIpInfoDto manageDto = hostBaseInfoDao.selectManagementIpDtoByHost(containerDto.getHostId());
        if (manageDto == null) {
            LOGGER.debug("HostManagementIpInfoDto is empty");
            return;
        }

        if (StringUtils.isEmpty(manageDto.getIpAddress())) {
            LOGGER.debug("manageDto.getIpAddress() is empty");
            return;
        }
        String hostIp = manageDto.getIpAddress();

        DiskSchemeService diskSchemeService=new DiskSchemeServiceImpl();
        DiskSchemeDto diskSchemeDto = diskSchemeService.queryDiskSchemeByid(param.getDiskSchemeId());
        if (diskSchemeDto == null) {
            LOGGER.debug("DiskSchemeDto is empty");
            return;
        }
        HostSshClient hostSshClient = Env.getSshClient(containerDto.getHostId() + ":" + hostIp);//创建 ssh 连接
        String statusCmd = "docker inspect  " + param.getId() + " |grep \"Running\" ";
        LOGGER.debug("statusCmd:[{}]", statusCmd);

        String result = hostSshClient.exec(statusCmd);
        if (result.contains("true")) {//容器是运行的
            String diskDirectoryCmd = "docker exec "+" "+param.getId()+" "+"df -h | grep ^zpool-docker/ |awk '{print $1}' | line 1 ";
            String diskDirectory = hostSshClient.exec(diskDirectoryCmd);//查询该容器对应的磁盘目录
            String upDiskSizeCmd = "zfs set quota=" + diskSchemeDto.getSize() + "MB" + " " + diskDirectory;
            LOGGER.debug("Change diskScheme cmd:[{}]", upDiskSizeCmd);
            hostSshClient.exec(upDiskSizeCmd);//修改容器对应磁盘大小
            containerDao.changeDiskScheme(param.getId(), param.getDiskSchemeId());
        } else {//容器没有运行
            String startContainerCmd = "docker start" + " " + param.getId();
            hostSshClient.exec(startContainerCmd);//启动容器
            String diskDirectoryCmd = "docker exec "+" "+param.getId()+" "+"df -h | grep ^zpool-docker/ |awk '{print $1}' | line 1";
            String diskDirectory = hostSshClient.exec(diskDirectoryCmd);//查询该容器对应的磁盘目录
            String upDiskSizeCmd = "zfs set quota=" + diskSchemeDto.getSize() + "MB" + " " + diskDirectory;
            LOGGER.debug("Change diskScheme cmd:[{}]", upDiskSizeCmd);
            hostSshClient.exec(upDiskSizeCmd);//修改容器对应磁盘大小
            containerDao.changeDiskScheme(param.getId(), param.getDiskSchemeId());
            String stopContainerCmd = "docker stop" + " " + param.getId();
            hostSshClient.exec(stopContainerCmd);
        }
    }


    @Override
    public void changeCalculationScheme(ContainerChangeCalculationSchemeParam param) throws Exception {
        if (param == null) {
            LOGGER.debug("Params is empty");
            return;
        }

        if (StringUtils.isEmpty(param.getId())) {
            LOGGER.debug("Container id is empty");
            return;
        }
        ContainerDto containerDto = containerDao.selectContainerDtoById(param.getId());
        if (containerDto == null) {
            LOGGER.debug("ContainerDto is empty");
            return;
        }
        if (StringUtils.isEmpty(param.getCalculationSchemeId())) {
            LOGGER.debug("CalculationScheme id is empty");
            return;
        }

        CalculationSchemeService calculationSchemeService=new CalculationSchemeServiceImpl();
        CalculationSchemeDto calculationSchemeDto =calculationSchemeService.queryCalculationSchemeInfo(param.getCalculationSchemeId());

        if (calculationSchemeDto == null){
            LOGGER.debug("CalculationSchemeDto is empty");
            return;
        }

        HostManagementIpInfoDto manageDto = hostBaseInfoDao.selectManagementIpDtoByHost(containerDto.getHostId());
        if (manageDto == null) {
            LOGGER.debug("HostManagementIpInfoDto is empty");
            return;
        }

        if (StringUtils.isEmpty(manageDto.getIpAddress())) {
            LOGGER.debug("manageDto.getIpAddress() is empty");
            return;
        }
        String hostIp = manageDto.getIpAddress();
        HostSshClient hostSshClient = Env.getSshClient(containerDto.getHostId() + ":" + hostIp);
        String cmd = "docker update  --cpu-shares" + " " + calculationSchemeDto.getCpuShares() + " " + "--memory" + " " + calculationSchemeDto.getMemory() + "MB" + " " + containerDto.getId();
        LOGGER.debug("Change calculationScheme cmd:[{}]", cmd);
        hostSshClient.exec(cmd);
        containerDao.changeCalculationScheme(param.getId(), param.getCalculationSchemeId());
    }


    @Override
    public void operationContainer(ContainerOpParam param) throws Exception {
        operationContainer(param.getId(), param.getOp());
    }


    @Override
    public void operationContainer(String id, String op) throws Exception {
        ContainerBasicService c = null;
        ContainerDto containerDto = null;
        ContainerDto dto = null;
        String host = null;
        String ip = null;
        switch (op) {
            case "start":
                if (StringUtils.isEmpty(id))
                    throw new Exception("id  is empty");
                containerDto = queryContainerDtoById(id);
                if (containerDto == null)
                    throw new Exception("containerDto  is empty");
                host = containerDto.getHostId();
                if (StringUtils.isEmpty(host))
                    throw new Exception("The host:[{" + host + "}] does not exist");
                ip = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
                if (StringUtils.isEmpty(ip))
                    throw new Exception("The managementIp:[{" + ip + "}] does not exist");
                c = ContainerBasicService.getContainerBasicService(id, host, ip);
                if (c == null)
                    throw new Exception("get ContainerBasicService fail");
                if (!c.exist(id))
                    throw new Exception("The container:[{" + id + "}] does not exist");
                dto = new ContainerDto();
                dto.setId(id);
                dto.setStatus(c.START());
                dto.setLastModifyTime(new Date());
                containerDao.updateContainer(dto);
                break;
            case "restart":
                if (StringUtils.isEmpty(id))
                    throw new Exception("id  is empty");
                containerDto = queryContainerDtoById(id);
                if (containerDto == null)
                    throw new Exception("containerDto  is empty");
                host = containerDto.getHostId();
                if (StringUtils.isEmpty(host))
                    throw new Exception("The host:[{" + host + "}] does not exist");
                ip = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
                if (StringUtils.isEmpty(ip))
                    throw new Exception("The managementIp:[{" + ip + "}] does not exist");
                c = ContainerBasicService.getContainerBasicService(id, host, ip);
                if (c == null)
                    throw new Exception("get ContainerBasicService fail");
                if (!c.exist(id))
                    throw new Exception("The container:[{" + id + "}] does not exist");
                dto = new ContainerDto();
                dto.setId(id);
                dto.setStatus(c.RESTART());
                dto.setLastModifyTime(new Date());
                containerDao.updateContainer(dto);
                break;
            case "stop":
                if (StringUtils.isEmpty(id))
                    throw new Exception("id  is empty");
                containerDto = queryContainerDtoById(id);
                if (containerDto == null)
                    throw new Exception("containerDto  is empty");
                host = containerDto.getHostId();
                if (StringUtils.isEmpty(host))
                    throw new Exception("The host:[{" + host + "}] does not exist");
                ip = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
                if (StringUtils.isEmpty(ip))
                    throw new Exception("The managementIp:[{" + ip + "}] does not exist");
                c = ContainerBasicService.getContainerBasicService(id, host, ip);
                if (c == null)
                    throw new Exception("get ContainerBasicService fail");
                if (!c.exist(id))
                    throw new Exception("The container:[{" + id + "}] does not exist");
                dto = new ContainerDto();
                dto.setId(id);
                dto.setStatus(c.STOP());
                dto.setLastModifyTime(new Date());
                containerDao.updateContainer(dto);
                break;
            case "pause":
                if (StringUtils.isEmpty(id))
                    throw new Exception("id  is empty");
                containerDto = queryContainerDtoById(id);
                if (containerDto == null)
                    throw new Exception("containerDto  is empty");
                host = containerDto.getHostId();
                if (StringUtils.isEmpty(host))
                    throw new Exception("The host:[{" + host + "}] does not exist");
                ip = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
                if (StringUtils.isEmpty(ip))
                    throw new Exception("The managementIp:[{" + ip + "}] does not exist");
                c = ContainerBasicService.getContainerBasicService(id, host, ip);
                if (c == null)
                    throw new Exception("get ContainerBasicService fail");
                if (!c.exist(id))
                    throw new Exception("The container:[{" + id + "}] does not exist");
                dto = new ContainerDto();
                dto.setId(id);
                dto.setStatus(c.PAUSE());
                dto.setLastModifyTime(new Date());
                containerDao.updateContainer(dto);
                break;
            case "unpause":
                if (StringUtils.isEmpty(id))
                    throw new Exception("id  is empty");
                containerDto = queryContainerDtoById(id);
                if (containerDto == null)
                    throw new Exception("containerDto  is empty");
                host = containerDto.getHostId();
                if (StringUtils.isEmpty(host))
                    throw new Exception("The host:[{" + host + "}] does not exist");
                ip = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
                if (StringUtils.isEmpty(ip))
                    throw new Exception("The managementIp:[{" + ip + "}] does not exist");
                c = ContainerBasicService.getContainerBasicService(id, host, ip);
                if (c == null)
                    throw new Exception("get ContainerBasicService fail");
                if (!c.exist(id))
                    throw new Exception("The container:[{" + id + "}] does not exist");
                dto = new ContainerDto();
                dto.setId(id);
                dto.setStatus(c.UNPAUSE());
                dto.setLastModifyTime(new Date());
                containerDao.updateContainer(dto);
                break;
            case "delete":
                //校验
                if (StringUtils.isEmpty(id))
                    return;
                containerDto = queryContainerDtoById(id);
                if (containerDto == null) {
                    LOGGER.debug("The container:[{}]  containerDto is empty", id);
                    return;
                }
                host = containerDto.getHostId();
                if (StringUtils.isEmpty(host)) {
                    LOGGER.debug("The host:[{}] is empty", host);
                    return;
                }
                ip = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
                if (StringUtils.isEmpty(ip)) {
                    LOGGER.debug("The managementIp:[{}] is empty", ip);
                    return;
                }

                c = ContainerBasicService.getContainerBasicService(id, host, ip);
                if (c == null) {
                    LOGGER.debug("get ContainerBasicService  fail");
                    return;
                }

                if (!c.exist(id)) {
                    LOGGER.debug("The container:[{" + id + "}] does not exist", id);
                    return;
                }

                //删除应用
                applicationService.applicationDeleteByContainer(id);

                //解除容器与网络标签关联
                NetService netService = new NetServiceImpl();
                List<NetLabelInfoDto> list = netService.netLabelInfoQueryByContainer(id);
                if (list != null && !list.isEmpty()) {
                    LOGGER.debug("size:[{}]", list.size());
                    for (NetLabelInfoDto n : list) {
                        DisconnectFromNetParam disconnectFromNetParam = new DisconnectFromNetParam();
                        disconnectFromNetParam.setLabel(n.getLabel());
                        disconnectFromNetParam.setContainer(id);
                        netService.containerDisconnectFromNetwork(disconnectFromNetParam);
                    }
                }
                c.DELETE();
                //删除容器
                containerDao.deleteContainerById(id);
                break;
            case "kill":
                if (StringUtils.isEmpty(id))
                    throw new Exception("id  is empty");
                containerDto = queryContainerDtoById(id);
                if (containerDto == null)
                    throw new Exception("containerDto  is empty");
                host = containerDto.getHostId();
                if (StringUtils.isEmpty(host))
                    throw new Exception("The host:[{" + host + "}] does not exist");
                ip = hostBaseInfoDao.selectManagementIpDtoByHost(host).getIpAddress();
                if (StringUtils.isEmpty(ip))
                    throw new Exception("The managementIp:[{" + ip + "}] does not exist");
                c = ContainerBasicService.getContainerBasicService(id, host, ip);
                if (c == null)
                    throw new Exception("get ContainerBasicService fail");
                if (!c.exist(id))
                    throw new Exception("The container:[{" + id + "}] does not exist");
                dto = new ContainerDto();
                dto.setId(id);
                dto.setStatus(c.KILL());
                dto.setLastModifyTime(new Date());
                containerDao.updateContainer(dto);
                break;
            default:
                throw new Exception("Operation:" + op + "does not exist");
        }
    }
}
