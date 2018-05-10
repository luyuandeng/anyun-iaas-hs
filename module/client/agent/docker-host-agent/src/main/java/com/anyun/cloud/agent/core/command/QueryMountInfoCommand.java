package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.agent.result.StorageInfo;
import com.anyun.cloud.tools.StringUtils;
import com.anyun.cloud.tools.bash.BashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询文件系统挂载信息
 *
 * Created by twitchgg on 16-8-3.
 */
public class QueryMountInfoCommand implements BaseLinuxCommandExecuter<List<StorageInfo>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueryMountInfoCommand.class);
    private String storageType;

    public QueryMountInfoCommand(String type) {
        if(type.toLowerCase().contains("gluster"))
            type = "fuse.glusterfs";
        storageType = type;
    }

    @Override
    public List<StorageInfo> exec() throws ApiException {
        if(storageType.equals("gluster"))
            storageType = "fuse.glusterfs";
        String cmd = "mount | grep 'type " + storageType + "' | awk -F ' ' '{print $3}'";
        LOGGER.debug("Command [{}]",cmd);
        String result = cmd(cmd);
        if (StringUtils.isEmpty(result))
            throw new ApiException("not find mount info by storage type [" + storageType + "]");
        List<String> destPaths = StringUtils.readStringLines(result);
        List<StorageInfo> storageInfos = new ArrayList<>();
        for (String destPath : destPaths) {
            cmd = "df -h " + destPath + " | sed '1d' | awk -F ' ' '{print $1,$2,$3,$4,$5,$6}'";
            try {
                result = cmd(cmd);
                storageInfos.add(parseInfo(result));
            } catch (ApiException ex) {
                continue;
            }
        }
        return storageInfos;
    }

    public StorageInfo exec(String mountedOn) throws ApiException {
        List<StorageInfo> storageInfos = exec();
        for (StorageInfo info : storageInfos) {
            if(info.getMountedOn().equals(mountedOn))
                return info;
        }
        return null;
    }



    private StorageInfo parseInfo(String result) {
        String[] infos = StringUtils.getSplitValues(result, " ");
        String fileSystem = infos[0];
        float size = convertSizeToGB(infos[1]);
        float used = convertSizeToGB(infos[2]);
        float avail = convertSizeToGB(infos[3]);
        float usedP = Float.valueOf(infos[4].replace("%", "").replace("-","0"));
        String dest = infos[5];
        StorageInfo storageInfo = new StorageInfo();
        storageInfo.setFilesystem(fileSystem);
        storageInfo.setSize(size);
        storageInfo.setUsed(used);
        storageInfo.setAvail(avail);
        storageInfo.setUsedPercentage(usedP);
        storageInfo.setMountedOn(dest);
        return storageInfo;
    }

    private float convertSizeToGB(String size) {
        size = size.toLowerCase();
        if(size.equals("0"))
            return 0;
        if (size.contains("m")) {
            return Float.valueOf(size.replace("m", "")).floatValue() / 1024F;
        }
        if (size.contains("t")) {
            return Float.valueOf(size.replace("t", "")).floatValue() * 1024F;
        }
        return Float.valueOf(size.replace("g", ""));
    }

    private String cmd(String cmd) throws ApiException {
        BashCommand bashCommand = new BashCommand(cmd);
        String result = bashCommand.exec();
        if (bashCommand.getException() != null)
            throw new ApiException(result, bashCommand.getException());
        return result.trim();
    }
}
