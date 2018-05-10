package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.tools.FileUtil;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.bash.BashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 挂载容器卷
 *
 * Created by twitchgg on 16-8-10.
 */
public class MountContainerVolumeCommand implements BaseLinuxCommandExecuter<Boolean> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MountContainerVolumeCommand.class);
    private static final String PATH_VOLUME_MOUNT_BASE = "/tmp/docker/volume";
    private static final String SCRIPT_PATH = "/var/lib/anyuncloud-agent/script/addvolume.sh";
    private String containerId;
    private String volumeId;
    private String containerMountPath;

    public MountContainerVolumeCommand(String containerId,String volumeId,String containerMountPath){
        this.containerId = containerId;
        this.volumeId = volumeId;
        this.containerMountPath = containerMountPath;
    }

    public MountContainerVolumeCommand(String volumeId,String containerMountPath){
        this.volumeId = volumeId;
        this.containerMountPath = containerMountPath;
    }

    @Override
    public Boolean exec() throws ApiException {
        String blockPath = "/storage/docker/volumes/" + volumeId;
        if (!new File(blockPath).exists())
            throw new ApiException("Volume block [" + blockPath + "] not exist");
        String mountHostPath = PATH_VOLUME_MOUNT_BASE + "/" + volumeId + "/";
        LOGGER.debug("mkdir -p [{}]", mountHostPath);
        FileUtil.mkdir(mountHostPath, false);
        LOGGER.debug("mount block to path [{}]", mountHostPath);
        String cmd = "mount -o loop " + blockPath + " " + mountHostPath;
        BashCommand command = new BashCommand(cmd);
        String result = command.exec();
        LOGGER.debug(result);
        if(result.contains("mounted"))
            return true;
        if (command.getException() != null){
            throw new ApiException(result);
        }
        cmd = "sh " + SCRIPT_PATH + " " + containerId + " " + containerMountPath;
        LOGGER.debug("Container mount cmd [{}]", cmd);
        command = new BashCommand(cmd);
        result = command.exec();
        if (command.getException() != null)
            throw new ApiException(result);
        String lastCommand = "df -h " + PATH_VOLUME_MOUNT_BASE + "/" + volumeId + "| sed 'id' | awk -F ' ' '{print $1}'";
        command = new BashCommand(lastCommand);
        result = command.exec();
        if(result.contains("loop"))
            return true;
        return false;
    }


    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
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
