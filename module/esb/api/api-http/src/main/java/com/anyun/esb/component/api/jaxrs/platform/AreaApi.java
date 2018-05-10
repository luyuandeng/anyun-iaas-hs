package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by sxt on 17-3-22.
 */
@Path("/area")
public class AreaApi {
    /**
     * 1、查询区域详情
     *
     * @param id           String   true
     * @param userUniqueId
     * @retuen
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "area.query.by.id",
            service = "area_query_by_id"
    )
    public String getDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 2、查询区域列表
     *
     * @param userUniqueId
     * @param type         类型： 两种 (KVM ,Docker)   String  false
     * @param status       状态 ： Enable(可用),Disable(禁用)   String   false
     * @retuen
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "area.query.by.typeOrStatus",
            service = "area_query_by_typeOrStatus"
    )
    public String getList(String type, String status, String userUniqueId) {
        return null;
    }

    /**
     * 3、删除区域
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
            operate = "area.delete",
            service = "area_delete"
    )
    public String delete(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 4、创建区域
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
            operate = "area.create",
            service = "area_create"
    )
    public String create(String body) {
        return null;
    }

    /**
     * 5、修改区域
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
            operate = "area.update",
            service = "area_update"
    )
    public String update(String body) {
        return null;
    }

    /**
     * 6、修改状态
     *
     * @param
     * @param
     * @retuen Status<String>
     */
    @POST
    @Path("/changeStatus")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "area.change.status",
            service = "area_change_status"
    )
    public String changeStatus(String body) {
        return null;
    }


    /**
     * 7、查询区域列根据条件
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
            operate = "area.query.by.conditions",
            service = "area_query_by_conditions"
    )
    public String getPageListConditions(@PathParam("s" ) String  s) {
        return null;
    }
}
