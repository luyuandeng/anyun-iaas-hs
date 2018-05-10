package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.bash.BashCommand;

/**
 * 查找文件系统是否存在
 *
 * Created by twitchgg on 16-8-3.
 */
public class FileSystemExistCommand implements BaseLinuxCommandExecuter<Boolean> {
    private String fileSystemType = "";
    private String fileSystem = "";
    private String mountedOn = "";

    @Override
    public Boolean exec() throws ApiException {
        StringBuilder cmdBuilder = new StringBuilder();
        if(fileSystem.equals("gluster"))
            fileSystem = "fuse.glusterfs";
        cmdBuilder.append("mount | grep 'type ").append(fileSystemType);
        cmdBuilder.append("' | grep '" + fileSystem + "' | awk -F ' ' '{print $3}'");
        BashCommand bashCommand = new BashCommand(cmdBuilder.toString());
        String result = bashCommand.exec();
        if (bashCommand.getException() != null)
            throw new ApiException(bashCommand.getException());
        if (StringUtils.isEmpty(result))
            return false;
        if(!result.equals(mountedOn))
            return false;
        return true;
    }

    public String getFileSystemType() {
        return fileSystemType;
    }

    public void setFileSystemType(String fileSystemType) {
        this.fileSystemType = fileSystemType;
    }

    public String getFileSystem() {
        return fileSystem;
    }

    public void setFileSystem(String fileSystem) {
        this.fileSystem = fileSystem;
    }

    public String getMountedOn() {
        return mountedOn;
    }

    public void setMountedOn(String mountedOn) {
        this.mountedOn = mountedOn;
    }
}
