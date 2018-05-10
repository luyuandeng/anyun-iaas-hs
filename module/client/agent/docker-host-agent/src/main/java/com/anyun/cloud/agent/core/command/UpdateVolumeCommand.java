package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.tools.bash.BashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by sugs on 16-8-18.
 *
 * 更改卷大小
 */
public class UpdateVolumeCommand implements BaseLinuxCommandExecuter{
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateVolumeCommand.class);
    private static final String PATH_VOLUMES_MOUNT_BASE = "/storage/docker/volumes";
    private static final String PATH_VOLUME_MOUNT_BASE = "/tmp/docker/volume";
    private String volumeId;
    private int size;

    public UpdateVolumeCommand(String volumeId,int size){
        this.volumeId = volumeId;
        this.size = size;
    }

    @Override
    public Boolean exec() throws ApiException {
        String PATH = PATH_VOLUMES_MOUNT_BASE + "/" +volumeId;
        if(!new File(PATH).exists())
            throw new ApiException("Volume block [" + PATH + "] not exist");
        String cmd = "e2fsck -f " + PATH;
        LOGGER.debug("updateVolumeCommand first cmd is" + cmd);
        BashCommand bashCommand = new BashCommand(cmd);
        String result = bashCommand.exec();
        if(bashCommand.getException() != null)
            LOGGER.debug("updateVolumeCommand first cmd get ERROR : " + result);
        cmd = "resize2fs " + PATH + " " + size + "g";
        bashCommand = new BashCommand(cmd);
        result = bashCommand.exec();
        if(bashCommand.getException() != null)
            LOGGER.debug("updateVolumeCommand last cmd get ERROR : " + result);
        return true;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
