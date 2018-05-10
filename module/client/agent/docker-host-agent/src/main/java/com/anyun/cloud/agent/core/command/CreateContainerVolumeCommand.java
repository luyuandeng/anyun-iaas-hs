package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.bash.BashCommand;

/**
 * 创建并且格式化卷(EXT4)
 * <p>
 * Created by twitchgg on 16-8-10.
 */
public class CreateContainerVolumeCommand implements BaseLinuxCommandExecuter<String> {
    private static final String CMD_TRUNCATE = "/usr/bin/truncate";
    private static final String CMD_MKFS = "/sbin/mkfs.ext4";

    private int size = 10;  //default block size
    private String path = "/storage/docker/volumes";    //BASE PATH by docker volume
    private String id;      //default is uuid generate
    private boolean format = true;

    public CreateContainerVolumeCommand(int size) {
        this.size = size;
    }

    @Override
    public String exec() throws ApiException {
        if (StringUtils.isEmpty(id))
            id = StringUtils.uuidGen();
        String blockPath = path + "/" + id;
        String cmd = CMD_TRUNCATE + " -s " + size + "g " + blockPath;
        BashCommand command = new BashCommand(cmd);
        String result = command.exec();
        if (command.getException() != null)
            throw new ApiException(result);
//        if (!format)
//            return true;
        cmd = CMD_MKFS + " -F " + blockPath;
        command = new BashCommand(cmd);
        result = command.exec();
        if (command.getException() != null)
            throw new ApiException(result);
        return id;
    }

    public int getSize() {
        return size;
    }

    public String getPath() {
        return path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isFormat() {
        return format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }
}
