package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;
import org.apache.camel.Header;

import javax.ws.rs.*;

/**
 * 应用 发布
 * Created by sxt on 16-10-24.
 */
@Path("/applications")
public class ApplicationApi {
    /**
     * 1、查询 应用详情
     *
     * @param id           应用Id
     * @param userUniqueId String  false  用户标识
     * @return ApplicationDto
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "application.query.by.id",
            service = "application_query_by_id")
    public String getDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }


    /**
     * 2、很据项目查询应用列表
     * @param project      String   项目Id
     * @param userUniqueId String   false  用户标识
     * @return List<ApplicationInfoDto>
     */
    @GET
    @Path("/list/{project}")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "application.query.by.project",
            service = "application_query_by_project")
    public String getListByProject(@PathParam("project") String project, String userUniqueId) {
        return null;
    }


    /**
     * 3、删除应用
     *
     * @param id
     * @param userUniqueId String  false  用户标识
     * @return Status<String>
     */
    @DELETE
    @Path("/delete/{id}")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "application.delete.by.id",
            service = "application_delete_by_id")
    public String delete(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 4、创建应用
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
            operate = "application.create",
            service = "application_create")
    public String create(String body) {
        return null;
    }

    /**
     * 5、查询应用列根据条件
     *
     *
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
            operate = "application.query.by.conditions",
            service = "application_query_by_conditions"
    )
    public String getPageListConditions(@PathParam("s" ) String  s) {
        return null;
    }

    /**
     * 6.根据条件查询负载
     */
    @GET
    @Path("/list/load/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "application.load.query.by.conditions",
            service = "application_load_query_by_conditions"
    )
    public String getPageListApplicationLoad(@PathParam("s" ) String  s) {
        return null;
    }

    /**
     * 7、 操作负载
     *
     * @param  body
     * @return Status<String>
     */
    @POST
    @Path("/load/operation")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "load.operation",
            service = "load_operation")
    public String operationLoad(String body) {
        return null;
    }


    /**
     * 8、 添加负载
     *
     * @param  body
     * @return Status<String>
     */
    @PUT
    @Path("/load/add")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "load.add",
            service = "load_add")
    public String addLoad(String body) {
        return null;
    }



    /**
     * 9、重新发布应用
     *
     * @param body
     * @return ApplicationInfoDto
     */
    @POST
    @Path("/republish")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "application.republish",
            service = "application_republish")
    public String republish(String  body) {
        return null;
    }
}
