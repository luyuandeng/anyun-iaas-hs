package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * 磁盘服务方案
 */
@Path("/diskScheme")
public class DiskSchemeApi {
    /**
     * 1、查询磁盘方案详情
     *
     * @param id
     * @param userUniqueId
     * @retuen DiskSchemeDto
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "diskScheme.query.by.id",
            service = "diskScheme_query_by_id"
    )
    public String getDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }


    /**
     * 2、查询磁盘方案列表
     *
     * @param userUniqueId
     * @retuen List<DiskSchemeDto>
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "diskScheme.list.query",
            service = "diskScheme_list_query"
    )
    public String getList(String userUniqueId) {
        return null;
    }


    /**
     * 3,根据条件分页查询磁盘方案列表
     */
    @GET
    @Path("/list/conditions/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "diskScheme.query.by.conditions",
            service = "diskScheme_query_by_conditions"
    )
    public String getPageListConditions(@PathParam("s") String s) {
        return null;
    }


    /**
     * 4、删除磁盘方案
     *
     * @param id
     * @param userUniqueId
     * @retuen Status<String>
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "diskScheme.delete.by.id",
            service = "diskScheme_delete_by_id"
    )
    public String delete(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 5、创建磁盘方案
     *
     * @param body
     * @retuen DiskSchemeDto
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "diskScheme.create",
            service = "diskScheme_create"
    )
    public String create(String body) {
        return null;
    }
}
