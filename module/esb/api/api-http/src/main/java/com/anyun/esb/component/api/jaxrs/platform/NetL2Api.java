package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by sxt on 17-4-7.
 */
@Path("/netL2")
public class NetL2Api {
    /**
     * 1、查询L2网络详情
     *
     * @param id           String   true
     * @param userUniqueId
     * @retuen NetL2InfoDto
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "netL2Info.query.by.id",
            service = "netL2Info_query_by_id"
    )
    public String getDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }


    /**
     * 2、查询L2网络列表
     *
     * @param userUniqueId
     * @param type         String      true     L2网络类型： DOCKER  只有一种类型
     * @retuen List<NetL2InfoDto>
     */
    @GET
    @Path("/list/{type}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "netL2Info.list.query.by.type",
            service = "netL2Info_list_query_by_type"
    )
    public String getList(@PathParam("type") String type, String userUniqueId) {
        return null;
    }

    /**
     * 3、删除L2网络
     *
     * @param id           主键  String  true
     * @param userUniqueId
     * @retuen Status<String>
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "netL2Info.delete.by.id",
            service = "netL2Info_delete_by_id"
    )
    public String delete(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 4、添加L2网络
     *
     * @param body
     * @retuen Status<String>
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "netL2Info.create",
            service = "netL2Info_create"
    )
    public String create(String body) {
        return null;
    }

    /**
     * 5、修改L2网络
     *
     * @param body
     * @retuen Status<String>
     */
    @POST
    @Path("/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "netL2Info.update",
            service = "netL2Info_update"
    )
    public String update(String body) {
        return null;
    }
}
