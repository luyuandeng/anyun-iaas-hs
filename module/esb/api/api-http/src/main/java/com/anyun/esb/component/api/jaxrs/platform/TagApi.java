package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by sxt on 17-5-4.
 */
@Path("/tag")
public class TagApi {
    /**
     * 1、查询标签列根据条件
     *
     * @retuen
     */
    @GET
    @Path("/list/conditions/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "tag.query.by.conditions",
            service = "tag_query_by_conditions"
    )
    public String getPageListConditions(@PathParam("s") String s) {
        return null;
    }


    /**
     * 2、删除标签
     *
     * @param id           String  true
     * @param userUniqueId
     * @retuen Status<String>
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "tag.delete",
            service = "tag_delete"
    )
    public String delete(@PathParam("id") String id, String userUniqueId) {
        return null;
    }


    /**
     * 3、添加标签
     *
     * @param
     * @param
     * @retuen
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "tag.create",
            service = "tag_create"
    )
    public String create(String body) {
        return null;
    }


    /**
     * 4、修改标签
     *
     * @param
     * @param
     * @retuen Status<String>
     */
    @POST
    @Path("/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "tag.update",
            service = "tag_update"
    )
    public String update(String body) {
        return null;
    }

    /**
     * 5、删除标签
     *
     * @param resourceId           String  true
     * @param userUniqueId
     * @retuen Status<String>
     */
    @DELETE
    @Path("/deleteByResourceId/{resourceId}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "tag.delete.resourceId",
            service = "tag_delete_resourceId"
    )
    public String deleteByResourceId(@PathParam("resourceId") String resourceId, String userUniqueId) {
        return null;
    }
}