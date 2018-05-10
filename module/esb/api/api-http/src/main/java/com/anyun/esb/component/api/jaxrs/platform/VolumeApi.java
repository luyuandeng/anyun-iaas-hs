package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by gp on 17-5-2.
 */
@Path("/volume")
public class VolumeApi {

    /**
     * 1、查询卷详情
     *
     * @param id           卷Id
     * @param userUniqueId
     * @return VolumeDto
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "volume.query.by.id",
            service = "volume_query_by_id")
    public String getVolumeDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 2、查询 卷 列表
     *
     * @param userUniqueId  String  false  用户标识
     * @param subMethod     String  true   子方法名称
     *                      QUERY_BY_CONTAINER： 根据容器Id 查询已经挂载的   卷列表
     *                      container String  true    容器Id
     *                      QUERY_BY_PROJECT：根据项目Id 查询 项目下的所有卷
     *                      project  String  true   项目Id
     *                      QUERY_UNMOUNTED_BY_PROJECT_CONTAINER:  根据项目Id和容器Id  查询一个项目里面 未挂此容器容 卷
     *                      project  String  true     项目Id
     *                      container String  true    容器Id
     *                      QUERY_BY_APPID： 根据应用Id  卷列表
     *                      appId  String  true    应用Id
     * @param subParameters 子方法对应的参数    格式如 ：p1|p2|p3...
     * @return List<VolumeDto> 卷信息列表
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "volume.query",
            service = "volume_query")
    public String getVolumeList(String userUniqueId, String subMethod, String subParameters) {
        return null;
    }

    /**
     * 3、删除卷
     *
     * @param id
     * @param userUniqueId
     * @return Status<String>
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "volume.delete.by.id",
            service = "volume_delete_by_id")
    public String deleteVolume(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 4、创建卷
     *
     * @param body
     * @return Status<String>
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "volume.create",
            service = "volume_create")
    public String createVolume(String body) {
        return null;
    }

    /**
     * 5、修改卷
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "volume.update",
            service = "volume_update")
    public String updateVolume(String body) {
        return null;
    }

    /**
     * 6、容器挂载卷
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/containerMountVolume")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "container.mount.volume",
            service = "container_mount_volume")
    public String containerMountVolume(String body) {
        return null;
    }


    /**
     * 7、容器卸载卷
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/containerUninstallVolume")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "container_uninstall_volume",
            service = "container_uninstall_volume")
    public String containerUninstallVolume(String body) {
        return null;
    }

    /**
     * 8、查询卷列表由存储--(新添接口)
     * @param id
     * @param userUniqueId
     * @return
     */
    @GET
    @Path("/queryVolumeByStorage/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "volume.query.by.storage",
            service = "volume_query_by_storage")
    public String queryVolumeByStorage(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 9、查询卷信息列表
     * @param s
     * @return
     */
    @GET
    @Path("/queryVolumeList/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "volume.query.list",
            service = "volume_query_list")
    public String queryVolumeList(@PathParam("s" ) String  s){return null;}

    /**
     * 10、根据容器Id或者卷Id查询 容器挂载卷信息
     * @param    container  String  false  容器Id
     * @param    volume     String  false  卷Id
     * @param    userUniqueId   String     用户唯一标示
     * @return   List<ContainerVolumeDto>
     */
    @GET
    @Path("/list/containerMountVolumeInfo")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-storage",
            operate = "container.mount.volume.info.query",
            service = "container_mount_volume_info_query")
    public String getContainerMountVolumeInfo(String  container,String  volume,String userUniqueId){return null;}
}
