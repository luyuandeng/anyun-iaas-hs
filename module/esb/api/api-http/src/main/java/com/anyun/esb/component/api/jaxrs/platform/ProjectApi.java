package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by sxt on 16-7-12.
 */
@Path("/project")
public class ProjectApi {
    /**
     *1、查询项目详情
     *
     * @param id 项目Id
     * @return ProjectDto
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "project.query.by.id",
            service = "project_query_by_id")
    public String getDetails(@PathParam("String") String id, String userUniqueId) {
        return null;
    }

    /**
     * 2、查询项目列表
     *
     * @param userUniqueId
     * @return List<ProjectDto>
     */
    @GET
    @Path("/list/{userUniqueId}")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "project.query.userUniqueId",
            service = "project_query_by_userUniqueId")
    public String getList(@PathParam("userUniqueId") String userUniqueId) {
        return null;
    }

    /**
     * 3、 删除项目
     *
     * @param
     * @return Status<String>
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "project.delete.by_id",
            service = "project_delete_by_id")
    public String deleteProject(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 4、创建项目
     *
     * @param
     * @return Status<String>
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "project.create",
            service = "project_create")
    public String createProject(String body) {
        return null;
    }

    /**
     *5、 修改项目
     *
     * @param
     * @return Status<String>
     */
    @POST
    @Path("/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "project.update.by_id",
            service = "project_update_by_id")
    public String update(String body) {
        return null;
    }


    /**
     *6、根据条件分页查询项目
     private boolean count = false;  //是否count查询，如果true，只返回条数
     private int start = 0; //分页offset start
     private int limit = 10;//分页pagesize
     private boolean replyWithCount = true;//返回结果是否包含总条数
     private List<Conditions> conditions;////查询条件列表
     private String sortBy = "";//排序字段
     private String sortDirection = "asc"; //排序方向  asc:正序（默认）   desc ：倒序
     *
     *
     * String  s   true
     *
     *

     {
     "count": false,
     "start": 1,
     "limit": 2,
     "replyWithCount": true,
     "conditions": [
     {
     "name": "type",
     "op": "=",
     "value": "'DOCKER'"
     }
     ],
     "sortBy": "name",
     "sortDirection": "asc"
     }

     *
     *
     * {"count":false,"start":1,"limit":2,"replyWithCount":true,"conditions":[{"name":"type","op":"=","value":"'DOCKER'"}],"sortBy":"name","sortDirection":"asc"}
     *
     *  CommonQueryParam  的 json  格式字符串
     *
     * @retuen
     */
    @GET
    @Path("/list/conditions/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "project.query.by.conditions",
            service = "project_query_by_conditions"
    )
    public String getPageListConditions(@PathParam("s" ) String  s) {
        return null;
    }

}