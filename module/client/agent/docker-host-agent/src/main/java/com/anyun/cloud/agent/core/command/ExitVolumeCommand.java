package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;

import java.io.File;

/**
 * Created by sugs on 16-9-21.
 */
public class ExitVolumeCommand implements BaseLinuxCommandExecuter<Boolean>{
    private String volumeId;

    public ExitVolumeCommand(String volumeId){
        this.volumeId = volumeId;
    }

    @Override
    public Boolean exec() throws ApiException {
        String blockPath = "/storage/docker/volumes/" + volumeId;
        if (!new File(blockPath).exists())
            return false;
        return true;
    }
}
