package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by sxt on 16-10-13.
 */
@Path("/picture")
public class PictureApi {
    /**
     * 1 查询所有图片列表
     * @param
     * @return <PictureDto>
     */
    @GET
    @Path("/allList")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-registry",
            operate = "picture.query.all",
            service = "picture_query_all")
    public String getAllList() {
        return null;
    }


    /**
     *
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
            component = "anyun-registry",
            operate = "picture.query.by.conditions",
            service = "picture_query_by_conditions"
    )
    public String getPageListConditions(@PathParam("s" ) String  s) {
        return null;
    }
}
