package com.anyun.esb.component.host.service;


import com.anyun.cloud.tools.StringUtils;
import com.anyun.esb.component.host.client.HostSshClient;
import com.anyun.esb.component.host.common.Env;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sxt on 17-5-15.
 */
public class ContainerBasicService {
    private final static Logger LOGGER = LoggerFactory.getLogger(ContainerBasicService.class);
    private DockerClient dockerClient = null; //docker 客户端
    private HostSshClient hostSshClient = null; //ssh   客户端
    private String id = "";//容器Id
    private String host = "";//宿主机Id
    private String ip = "";//宿主机管理ip

    /**
     * 赋值
     *
     * @param id   容器Id
     * @param host 宿主机Id
     * @param host 宿主机管理ip
     */
    private void assignment(String id, String host, String ip) throws Exception {
        this.id = id;
        this.host = host;
        this.ip = ip;
        if (dockerClient == null)
            dockerClient = Env.getDockerClient(ip);
        if (hostSshClient == null)
            hostSshClient = Env.getSshClient(host + ":" + ip);
    }

    private ContainerBasicService(String id, String host, String ip) throws Exception {
        assignment(id, host, ip);
    }


    /**
     * 获取构造对象
     *
     * @param id   容器Id
     * @param host 宿主机Id
     * @param host 宿主机管理ip
     * @return ContainerBasicService
     */
    public static ContainerBasicService getContainerBasicService(String id, String host, String ip) throws Exception {
        if (StringUtils.isEmpty(id))
            throw new Exception("id  is  empty");
        if (StringUtils.isEmpty(host))
            throw new Exception("host  is  empty");
        if (StringUtils.isEmpty(ip))
            throw new Exception("ip  is  empty");
        return new ContainerBasicService(id, host, ip);
    }


    /**
     * 判断容器是否存在
     *
     * @param id 容器id
     * @return true  or false
     */
    public boolean exist(String id) throws Exception {
        boolean flag = true;
        if (StringUtils.isEmpty(id))
            throw new Exception("id is empty");
        try {
            InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(id).exec();
            if (inspectContainerResponse == null || StringUtils.isEmpty(inspectContainerResponse.getId()))
                flag = false;
        } catch (Exception e) {
            LOGGER.debug(e.getMessage());
            flag = false;
        }
        LOGGER.debug("flag:[{}]", flag);
        return flag;
    }

    /**
     * 获取容器状态
     *
     * @param id 容器id
     * @return true  or false
     */
    public String getStatus(String id) throws Exception {
        if (!exist(id))
            throw new Exception("Container does not  exit");
        InspectContainerResponse inspectContainerResponse = dockerClient.inspectContainerCmd(id).exec();
        String s = inspectContainerResponse.getState().getStatus();
        String format = "(created)|(running)|(paused)|(exited)";
        if (!s.matches(format))
            throw new Exception("The container status:[" + s + "] is unknown");
        return s;
    }


    /**
     * 启动容器
     * 1:created(已创建)
     * 2:running (运行中)
     * 3:paused （暂停中）
     * 4:exited （已退出）
     */
    public int START() throws Exception {
        String status = getStatus(id);
        switch (status) {
            case "created":
                dockerClient.startContainerCmd(id).exec();
                break;
            case "running":
                break;
            case "paused":
                dockerClient.unpauseContainerCmd(id).exec();
                break;
            case "exited":
                dockerClient.startContainerCmd(id).exec();
                break;
        }
        if (!getStatus(id).equals("running"))
            throw new Exception("Failed to start container");
        return 2;
    }

    /**
     * 重启容器
     * 1:created(已创建)
     * 2:running (运行中)
     * 3:paused （暂停中）
     * 4:exited （已退出）
     */
    public int RESTART() throws Exception {
        String status = getStatus(id);
        switch (status) {
            case "created":
                break;
            case "running":
                break;
            case "paused":
                dockerClient.unpauseContainerCmd(id).exec();
                break;
            case "exited":
                break;
        }
        dockerClient.restartContainerCmd(id).exec();
        if (!getStatus(id).equals("running"))
            throw new Exception("Failed to restart container");
        return 2;
    }


    /**
     * 停止容器
     * 1:created(已创建)
     * 2:running (运行中)
     * 3:paused （暂停中）
     * 4:exited （已退出）
     */
    public int STOP() throws Exception {
        String status = getStatus(id);
        switch (status) {
            case "created":
                break;
            case "running":
                dockerClient.stopContainerCmd(id).exec();
                break;
            case "paused":
                dockerClient.unpauseContainerCmd(id).exec();
                dockerClient.stopContainerCmd(id).exec();
                break;
            case "exited":
                break;
        }
        if (!getStatus(id).equals("exited") && !getStatus(id).equals("created"))
            throw new Exception("Failed to stop container");
        return 4;
    }


    /**
     * 停止容器
     * <p>
     * 1:created(已创建)
     * 2:running (运行中)
     * 3:paused （暂停中）
     * 4:exited （已退出）
     */
    public int KILL() throws Exception {
        String status = getStatus(id);
        switch (status) {
            case "created":
                break;
            case "running":
                dockerClient.killContainerCmd(id).exec();
                break;
            case "paused":
                dockerClient.unpauseContainerCmd(id).exec();
                dockerClient.killContainerCmd(id).exec();
                break;
            case "exited":
                break;
        }
        if (!getStatus(id).equals("exited") && !getStatus(id).equals("created"))
            throw new Exception("Failed to kill container");
        return 4;
    }


    /**
     * 暂停容器
     * 1:created(已创建)
     * 2:running (运行中)
     * 3:paused （暂停中）
     * 4:exited （已退出）
     */
    public int PAUSE() throws Exception {
        String status = getStatus(id);
        switch (status) {
            case "created":
                break;
            case "running":
                dockerClient.pauseContainerCmd(id).exec();
                break;
            case "paused":
                break;
            case "exited":
                break;
        }
        if (!getStatus(id).equals("paused"))
            throw new Exception("Failed to pause container");
        return 3;
    }

    /**
     * 解除暂停
     * 1:created(已创建)
     * 2:running (运行中)
     * 3:paused （暂停中）
     * 4:exited （已退出）
     */
    public int UNPAUSE() throws Exception {
        String status = getStatus(id);
        switch (status) {
            case "created":
                break;
            case "running":
                break;
            case "paused":
                dockerClient.unpauseContainerCmd(id).exec();
                break;
            case "exited":
                break;
        }
        if (!getStatus(id).equals("running"))
            throw new Exception("Failed to UNPAUSE  container");
        return 2;
    }

    /**
     * 删除容器
     * 1:created(已创建)
     * 2:running (运行中)
     * 3:paused （暂停中）
     * 4:exited （已退出）
     */
    public void DELETE() throws Exception {
        String status = getStatus(id);
        switch (status) {
            case "created":
                break;
            case "running":
                dockerClient.killContainerCmd(id).exec();
                break;
            case "paused":
                dockerClient.unpauseContainerCmd(id).exec();
                dockerClient.killContainerCmd(id).exec();
                break;
            case "exited":
                break;
        }
        //强制删除容器
        dockerClient.removeContainerCmd(id).withForce(true).exec();
        if (exist(id)) {
            throw new Exception("Failed to delete container");
        }
    }

}
