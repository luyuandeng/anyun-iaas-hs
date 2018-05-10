package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by sxt on 16-12-6.
 */
@Path("/compose")
public class ComposeApi {
    /**
     * 查询编排 由 id
     *
     * @param id
     * @return ComposeDto
     * @apram userUniqueId
     */
    @GET
    @Path("/queryById")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "compose.query.by.id",
            service = "compose_query_by_id")
    public String composeQueryById(String id, String userUniqueId) {
        return null;
    }

    /**
     * 查询编排 由 用户
     *
     * @param userUniqueId
     * @return List<ComposeDto>
     */
    @GET
    @Path("/queryByUserUniqueId")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "compose.query.by.userUniqueId",
            service = "compose_query_by_userUniqueId")
    public String composeQueryByUserUniqueId(String userUniqueId) {
        return null;
    }

    /**
     * 创建编排
     *
     * @param body
     * @return Status<String>
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "compose.create",
            service = "compose_create")
    public String composeCreate(String body) {
        return null;
    }


    /**
     * 删除编排
     *
     * @param id
     * @param userUniqueId
     * @return Status<String>
     */
    @DELETE
    @Path("/delete")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "compose.delete.by.id",
            service = "compose_delete_by_id")
    public String composeDeleteById(String id, String userUniqueId) {
        return null;
    }

/*
    *//**
     * 修改编排
     *
     * @param body
     * @return Status<String>
     *//*
    @POST
    @Path("/update")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "compose.update",
            service = "compose_update")
    public String composeUpdate(String body) {
        return null;
    }*/
}
