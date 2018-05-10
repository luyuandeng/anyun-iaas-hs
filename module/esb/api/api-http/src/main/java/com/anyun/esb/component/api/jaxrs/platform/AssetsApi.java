package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

@Path("/assets")
public class AssetsApi {
    /**
     * 1、查询资产详情
     *
     * @param id
     * @param userUniqueId
     * @retuen
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "assets.query.by.id",
            service = "assets_query_by_id"
    )
    public String getDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }


    /**
     * 2、根据设备分类查询资产列表
     *
     * @param userUniqueId
     * @param deviceCategory String  true   分类：  SERVER服务器  ROUTER路由器  SWITCH交换机  OTHER其他设备   ALL:所有
     * @retuen
     */
    @GET
    @Path("/list/{deviceCategory}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "assets.list.query.by.deviceCategory",
            service = "assets_list_query_by_deviceCategory"
    )
    public String getListByDeviceCategory(@PathParam("deviceCategory") String deviceCategory, String userUniqueId) {
        return null;
    }


    /**
     * 3,根据条件分页查询资产列表
     */
    @GET
    @Path("/base/conditions/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "assets.query.by.conditions",
            service = "assets_query_by_conditions"
    )
    public String getPageListConditions(@PathParam("s") String s) {
        return null;
    }


    /**
     * 4、删除资产
     *
     * @param id           String   true
     * @param userUniqueId
     * @retuen Status<String>
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "assets.delete.by.id",
            service = "assets_delete_by_id"
    )
    public String deleteBase(@PathParam("id") String id, String userUniqueId) {
        return null;
    }


    /**
     * 5、批量创建资产
     *
     * @param body
     * @retuen List<AssetsDto>
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "assets.base.create",
            service = "assets_base_create"
    )
    public String create(String body) {
        return null;
    }


    /**
     * 6、修改资产
     *
     * @retuen Status<String>
     */
    @POST
    @Path("/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "assets.update",
            service = "assets_update"
    )
    public String update(String body) {
        return null;
    }


    /**
     * 6.批量删除资产管理
     *
     * @param
     * @param userUniqueId
     * @retuen Status<String>
     */
    @DELETE
    @Path("/deleteList/{ids}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "assets.delete.by.list",
            service = "assets_delete_by_list"
    )
    public String delete(@PathParam("ids") String ids, String userUniqueId) {
        return null;
    }

}
