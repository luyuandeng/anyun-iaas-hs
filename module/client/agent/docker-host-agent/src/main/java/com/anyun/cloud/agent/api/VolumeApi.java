package com.anyun.cloud.agent.api;

import com.anyun.cloud.agent.common.Utils;
import com.anyun.cloud.agent.common.service.VolumeService;
import com.anyun.cloud.agent.core.DefaultVolumeService;
import com.anyun.cloud.agent.result.Status;
import com.anyun.cloud.api.Response;
import com.anyun.cloud.param.MountVolumeParam;
import com.anyun.cloud.param.UmountVolumeParam;
import com.anyun.cloud.param.UpdateVolumeParam;
import com.anyun.cloud.param.VolumeCreateParamDocker;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by twitchgg on 16-8-10.
 */
@Path("volume")
public class VolumeApi {
    private VolumeService volumeService;

    public VolumeApi() {
        volumeService = new DefaultVolumeService();
    }

    //创建并格式化卷
    @Path("create")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String createVolume(VolumeCreateParamDocker param) {
        Response<Status<String>> response = volumeService.createVolume(param);
        return Utils.toJson(response);
    }

    //挂载卷
    @Path("mount")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String mountVolume(MountVolumeParam param){
        Response<Status<Boolean>> response = volumeService.mountVolume(param);
        return Utils.toJson(response);
    }

    //修改卷
    @Path("update")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateVolume(UpdateVolumeParam param){
        Response<Status<Boolean>> response = volumeService.updateVolume(param);
        return Utils.toJson(response);
    }

    //卸载卷
    @Path("umount")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String umountVolume(UmountVolumeParam param){
        Response<Status<Boolean>> response = volumeService.umountVolume(param);
        return Utils.toJson(response);
    }

    //删除卷
    @Path("delete/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteVolme(@PathParam("id")String id){
        Response<Status<Boolean>> response = volumeService.deleteVolume(id);
        return Utils.toJson(response);
    }

    //判断卷是否存在
    @Path("exit/{id}")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public String exitVolume(@PathParam("id")String id){
        Response<Status<Boolean>> response = volumeService.exitVolume(id);
        return Utils.toJson(response);
    }
}
