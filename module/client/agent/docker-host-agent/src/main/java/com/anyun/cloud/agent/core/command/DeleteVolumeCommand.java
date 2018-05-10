package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.tools.bash.BashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by sugs on 16-9-21.
 */
public class DeleteVolumeCommand implements BaseLinuxCommandExecuter<Boolean>{

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteVolumeCommand.class);

    private String volumeId;

    public DeleteVolumeCommand(String volumeId){
        this.volumeId = volumeId;
    }

    @Override
    public Boolean exec() throws ApiException {
        String blockPath = "/storage/docker/volumes/" + volumeId;
        if (!new File(blockPath).exists())
            throw new ApiException("Volume block [" + blockPath + "] not exist");
        String cmd = "rm " + blockPath;
        BashCommand bashCommand = new BashCommand(cmd);
        String result = bashCommand.exec();
        if(bashCommand.getException() != null)
            LOGGER.debug("updateVolumeCommand last cmd get ERROR : " + result);
        return true;
    }
}
