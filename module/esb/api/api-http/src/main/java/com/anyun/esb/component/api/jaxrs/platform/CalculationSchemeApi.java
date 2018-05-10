package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * 计算方案
 */
@Path("/calculationScheme")
public class CalculationSchemeApi {
    /**
     * 1、查询计算方案详情
     *
     * @param id
     * @param userUniqueId
     * @retuen CalculationSchemeDto
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "calculationScheme.query.by.id",
            service = "calculationScheme_query_by_id"
    )
    public String getDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }


    /**
     * 2、查询计算方案列表
     *
     * @param userUniqueId
     * @retuen List<CalculationSchemeDto>
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "calculationScheme.list.query",
            service = "calculationScheme_list_query"
    )
    public String getList(String userUniqueId) {
        return null;
    }


    /**
     * 3,根据条件分页查询计算方案列表
     */
    @GET
    @Path("/list/conditions/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "calculationScheme.query.by.conditions",
            service = "calculationScheme_query_by_conditions"
    )
    public String getPageListConditions(@PathParam("s") String s) {
        return null;
    }


    /**
     * 4、删除计算方案
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
            operate = "calculationScheme.delete.by.id",
            service = "calculationScheme_delete_by_id"
    )
    public String delete(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 5、创建计算方案
     *
     * @param body
     * @retuen CalculationSchemeDto
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "calculationScheme.create",
            service = "calculationScheme_create"
    )
    public String create(String body) {
        return null;
    }
}
