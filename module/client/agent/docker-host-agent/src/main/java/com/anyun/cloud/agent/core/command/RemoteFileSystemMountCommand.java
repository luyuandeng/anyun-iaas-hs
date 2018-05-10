package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.agent.result.StorageInfo;
import com.anyun.cloud.tools.bash.BashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 挂载文件系统
 *
 * Created by twitchgg on 16-8-3.
 */
public class RemoteFileSystemMountCommand implements BaseLinuxCommandExecuter<StorageInfo> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteFileSystemMountCommand.class);
    private String type;
    private String remoteURL;
    private String mountOn;

    @Override
    public StorageInfo exec() throws ApiException {
        mkDir(mountOn);
        StringBuilder sb = new StringBuilder();
        sb.append("mount -t ").append(type).append(" ");
        sb.append(remoteURL).append(" ");
        sb.append(mountOn);
        BashCommand bashCommand = new BashCommand(sb.toString());
        bashCommand.exec();
        LOGGER.debug("Remote FileSystem mount command [{}]",sb.toString());
        return new QueryMountInfoCommand(type).exec(mountOn);
    }

    private void mkDir(String dir) {
        String cmd = "mkdir -p " + dir;
        new BashCommand(cmd).exec();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemoteURL() {
        return remoteURL;
    }

    public void setRemoteURL(String remoteURL) {
        this.remoteURL = remoteURL;
    }

    public String getMountOn() {
        return mountOn;
    }

    public void setMountOn(String mountOn) {
        this.mountOn = mountOn;
    }
}
