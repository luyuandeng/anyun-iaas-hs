package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by sxt on 7/4/17.
 */
@Path("/db")
public class DatabaseApi {
    /**
     * 1、获取数据库详情
     *
     * @param id           主键
     * @param userUniqueId 用户唯一标识
     * @return DatabaseDto
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "database.details.query.by.id",
            service = "database_details_query_by_id")
    public String getDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }


    /**
     * 2、 分页查询数据库列表
     *
     * @param s CommonQueryParam  的 json  格式字符串
     * @retuen PageDto <DatabaseDto>
     */
    @GET
    @Path("/getPageList/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "database.pageList.query.by.condition",
            service = "database_pageList_query_by_condition"
    )
    public String getPageListByCondition(@PathParam("s") String s) {
        return null;
    }


    /**
     * 3、 删除数据库
     *
     * @param id           主键
     * @param userUniqueId 用户唯一标识
     * @return Status<String>
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "database.delete.by.id",
            service = "database_delete_by_id")
    public String delete(@PathParam("id") String id, String userUniqueId) {
        return null;
    }


    /**
     * 4、创建数据库
     *
     * @param body
     * @return DatabaseDto
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "database.create",
            service = "database_create")
    public String create(String body) {
        return null;
    }
}
