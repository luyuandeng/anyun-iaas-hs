package com.anyun.cloud.agent.core;

import com.anyun.cloud.agent.common.service.VolumeService;
import com.anyun.cloud.agent.core.command.*;
import com.anyun.cloud.agent.result.Status;
import com.anyun.cloud.api.Response;
import com.anyun.cloud.param.MountVolumeParam;
import com.anyun.cloud.param.UmountVolumeParam;
import com.anyun.cloud.param.UpdateVolumeParam;
import com.anyun.cloud.param.VolumeCreateParamDocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by twitchgg on 16-8-10.
 */
public class DefaultVolumeService implements VolumeService {
    private static Logger LOGGER = LoggerFactory.getLogger(DefaultVolumeService.class);

    @Override
    public Response<Status<String>> createVolume(VolumeCreateParamDocker param) {
        CreateContainerVolumeCommand createContainerVolumeCommand = new CreateContainerVolumeCommand(param.getSize());
        Response<Status<String>> response = new Response<>();
        response.setType("VolumeService.createVolume");
        try {
            String id = createContainerVolumeCommand.exec();
            response.setContent(new Status<>(id));
        } catch (Exception ex){
            response.setException(ex);
        }
        return response;
    }

    @Override
    public Response<Status<Boolean>> mountVolume(MountVolumeParam param) {
        MountContainerVolumeCommand mountContainerVolumeCommand = new MountContainerVolumeCommand(param.getContainerId(),param.getVolumeId(),param.getContainerMountPath());
        Response<Status<Boolean>> response = new Response<>();
        response.setType("VolumeService.mountVolume");
        try {
            Boolean bool = mountContainerVolumeCommand.exec();
            response.setContent(new Status<>(bool));
        } catch (Exception e){
            response.setException(e);
        }

        return response;
    }

    @Override
    public Response<Status<Boolean>> umountVolume(UmountVolumeParam param) {
        UmountVolumeCommand umountVolumeCommand = new UmountVolumeCommand(param.getContainerId(),param.getVolumeId(),param.getContainerMountPath());
        Response<Status<Boolean>> response = new Response<>();
        response.setType("VolumeService.umountVolume");
        try {
            Boolean bool = umountVolumeCommand.exec();
            response.setContent(new Status<>(bool));
        } catch (Exception e){
            response.setException(e);
        }
        return response;
    }

    @Override
    public Response<Status<Boolean>> updateVolume(UpdateVolumeParam param) {
        Response<Status<Boolean>> response = new Response<>();

        try {
            response = umountVolume(new UmountVolumeParam().setVolumeId(param.getVolumeId()));
        } catch (Exception e){
            response.setException(e);
            response.setType("VolumeService.updateVolume");
            return response;
        }

        UpdateVolumeCommand updateVolumeCommand = new UpdateVolumeCommand(param.getVolumeId(),param.getSize());
        response.setType("VolumeService.updateVolume");
        try {
            updateVolumeCommand.exec();
        } catch (Exception e){
            response.setException(e);
        }

        MountContainerVolumeCommand mountContainerVolumeCommand = new MountContainerVolumeCommand(param.getVolumeId(),param.getContainerMountPath());
        try {
            mountContainerVolumeCommand.exec();
        } catch (Exception e){
            response.setException(e);
        }
        return response;
    }

    @Override
    public Response<Status<Boolean>> deleteVolume(String id) {
        Response<Status<Boolean>> response = new Response<>();
        LOGGER.debug("deleteVolumes get id =" + id);
        DeleteVolumeCommand deleteVolumeCommand = new DeleteVolumeCommand(id);

        response.setType("VolumeService.deleteVolume");
        try {
            Boolean bool = deleteVolumeCommand.exec();
            response.setContent(new Status<>(bool));
        } catch (Exception e){
            response.setException(e);
        }

        return response;
    }

    @Override
    public Response<Status<Boolean>> exitVolume(String id) {
        LOGGER.debug("ExitVolumes get id =" + id);
        Response<Status<Boolean>> response = new Response<>();
        ExitVolumeCommand exitVolumeCommand = new ExitVolumeCommand(id);
        response.setType("VolumeService.ExitVolume");
        try {
            Boolean bool = exitVolumeCommand.exec();
            LOGGER.debug("ExitVolumes result = " + bool);
            response.setContent(new Status<>(bool));
        } catch (Exception e){
            response.setException(e);
        }
        return response;
    }
}
