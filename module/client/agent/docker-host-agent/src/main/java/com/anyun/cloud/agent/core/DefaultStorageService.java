package com.anyun.cloud.agent.core;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.agent.common.service.StorageService;
import com.anyun.cloud.agent.core.command.FileSystemExistCommand;
import com.anyun.cloud.agent.core.command.QueryMountInfoCommand;
import com.anyun.cloud.agent.core.command.RemoteFileSystemMountCommand;
import com.anyun.cloud.agent.param.GlusterStorageAdd;
import com.anyun.cloud.agent.result.StorageInfo;
import com.anyun.cloud.api.Response;
import com.anyun.cloud.tools.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by twitchgg on 16-8-3.
 */
public class DefaultStorageService implements StorageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStorageService.class);

    @Override
    public Response<List<StorageInfo>> listStorageByType(String type) {
        QueryMountInfoCommand queryMountInfoCommand = new QueryMountInfoCommand(type);
        Response<List<StorageInfo>> response = new Response<>();
        response.setType("StorageService.listStorageByType");
        List<StorageInfo> storageInfos = null;
        try {
            storageInfos = queryMountInfoCommand.exec();
            response.setContent(storageInfos);
        } catch (ApiException ex) {
            response.setException(ex);
        }
        return response;
    }

    @Override
    public Response<StorageInfo>  addStorage(String type, String json) {
        LOGGER.debug("STORAGE CREATE TYPE [{}]", type);
        LOGGER.debug("STORAGE CONFIGURATION [{}]", json);
        Response<StorageInfo> response = new Response<>();
        response.setType("StorageService.addStorage");
        StorageInfo storageInfo = null;
        try {
            if (type.equals("gluster")) {
                GlusterStorageAdd param = JsonUtil.fromJson(GlusterStorageAdd.class, json);
                storageInfo = addGlusterStorage(param);
            } else if (type.equals("yeestore")) {
                throw new UnsupportedOperationException(type);
            } else if (type.equals("nfs")) {
                throw new UnsupportedOperationException(type);
            } else {
                throw new UnsupportedOperationException(type);
            }
        } catch (Exception ex) {
            response.setException(new UnsupportedOperationException(type));
            return response;
        }
        response.setContent(storageInfo);
        return response;
    }

    private StorageInfo addGlusterStorage(GlusterStorageAdd param) throws Exception {
        String fileSystemType = "fuse.glusterfs";
        FileSystemExistCommand existCommand = new FileSystemExistCommand();
        existCommand.setFileSystem(param.getGluserSrc());
        existCommand.setFileSystemType(fileSystemType);
        String mountedOn = "";
        if (param.getUseType().equals("docker.runtime"))
            mountedOn = "/storage/docker/overlayfs";
        else if (param.getUseType().equals("docker.volume")) {
            mountedOn = "/storage/docker/volumes";
        } else
            throw new UnsupportedOperationException(param.getUseType());
        existCommand.setMountedOn(mountedOn);
        if (existCommand.exec()) {
            throw new Exception("[" + fileSystemType + "] FileSystem [" + param.getGluserSrc() + "] exist");
        }
        RemoteFileSystemMountCommand mountCommand = new RemoteFileSystemMountCommand();
        mountCommand.setType("glusterfs");
        mountCommand.setMountOn(mountedOn);
        mountCommand.setRemoteURL(param.getGluserSrc());
        return mountCommand.exec();
    }
}
