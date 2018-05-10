package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by sxt on 17-3-22.
 */
@Path("/cluster")
public class ClusterApi {
    /**
     * 1、查询集群详情
     *
     * @param id           String   true
     * @param userUniqueId String   false
     * @retuen ClusterDto
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "cluster.query.by.id",
            service = "cluster_query_by_id"
    )
    public String getDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 2、根据状态查询集群列表
     *
     * @param userUniqueId String   false
     * @param status       String   true
     *                     状态：
     *                     Enable(可用)
     *                     Disable(禁用)
     *                     All  查询全部
     * @retuen List<ClusterDto>
     */
    @GET
    @Path("/list/{status}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "cluster.query.list.by.status",
            service = "cluster_query_list_by_status"
    )
    public String getList(@PathParam("status") String status, String userUniqueId) {
        return null;
    }

    /**
     * 3、删除集群
     *
     * @param id           String  true
     * @param userUniqueId String   false
     * @retuen Status<String>
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "cluster.delete",
            service = "cluster_delete"
    )
    public String delete(@PathParam("id") String id, String userUniqueId) {
        return null;
    }


    /**
     * 4、创建集群
     *
     * @param body
     * @retuen ClusterDto
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "cluster.create",
            service = "cluster_create"
    )
    public String create(String body) {
        return null;
    }

    /**
     * 5、修改集群
     *
     * @param body
     * @retuen ClusterDto
     */
    @POST
    @Path("/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "cluster.update",
            service = "cluster_update"
    )
    public String update(String body) {
        return null;
    }


    /**
     * 6、更改状态
     *
     * @param body
     * @retuen Status<String>
     */
    @POST
    @Path("/changeStatus")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "cluster.change.status",
            service = "cluster_change_status"
    )
    public String changeStatus(String body) {
        return null;
    }
}
