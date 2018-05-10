package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.tools.bash.BashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sugs on 16-8-18.
 * 卸载卷
 */
public class UmountVolumeCommand implements BaseLinuxCommandExecuter{
    private static final Logger LOGGER = LoggerFactory.getLogger(UmountVolumeCommand.class);
    private static final String PATH_VOLUMES_MOUNT_BASE = "/tmp/docker/volume";
    private String volumeId;
    private String containerId;
    private String containerMountPath;

    public UmountVolumeCommand(String containerId,String volumeId,String containerMountPath){
        this.containerId = containerId;
        this.volumeId = volumeId;
        this.containerMountPath = containerMountPath;
    }

    @Override
    public Boolean exec() throws ApiException {
//        String cmd = "/usr/bin/docker-enter " + containerId + " umount " + containerMountPath;
//        BashCommand bashCommand = new BashCommand(cmd);
//        String result = bashCommand.exec();
//        if(bashCommand.getException() != null)
//            LOGGER.debug("UmountVolumeCommand first cmd get ERROR : " + result);
        String path = PATH_VOLUMES_MOUNT_BASE + "/" + volumeId;
        LOGGER.debug("UmountVolumeCommand's path is " + path);
        String cmd = "umount " + path;
        BashCommand bashCommand = new BashCommand(cmd);
        String result = bashCommand.exec();
        if(bashCommand.getException() != null)
            LOGGER.debug("UmountVolumeCommand last cmd get ERROR : " + result);
        return true;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getContainerMountPath() {
        return containerMountPath;
    }

    public void setContainerMountPath(String containerMountPath) {
        this.containerMountPath = containerMountPath;
    }
}
