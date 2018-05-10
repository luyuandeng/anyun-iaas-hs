package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;
import java.util.Map;


/**
 * Created by gaopeng on 16-6-7.
 */
@Path("/containers")
public class ContainerApi {
    /**
     * 1、查询容器详情
     *
     * @param id
     * @param userUniqueId
     * @return ContainerDto
     */
    @GET
    @Path("/details/{id}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "container.query.by_id",
            service = "container_query_by_id")
    public String getDetails(@PathParam("id") String id, String userUniqueId) {
        return null;
    }

    /**
     * 2、查询  容器列表
     *
     * @param userUniqueId  String  false  用户标识
     * @param subMethod     String  true   子方法名称
     *                      QUERY_BY_IMAGE ：根据镜像Id 查询容器列表
     *                      image       String   true   镜像Id
     *                      QUERY_BY_LABEL： 根据网络标签 查询容器列表
     *                      label       String  true    网络标签
     *                      QUERY_NOT_CONNECTED_BY_LABEL  根据网络标签 查询  未连接到该网络的  容器列表
     *                      label       String  true    网络标签
     *                      QUERY_BY_PROJECT：根据项目Id 查询容器列表
     *                      project     String  true    项目Id
     *                      QUERY_UNPUBLISHED_BY_PROJECT：      查询一个项目里面 未发布的应用的  WEB 容器列表
     *                      project     String    true 项目Id
     * @param subParameters String  true   子参数  子方法对应的参数  格式如  p1|p2|p3
     * @return List<ContainerDto>
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "container.list",
            service = "container_list")
    public String getList(String userUniqueId, String subMethod, String subParameters) {
        return null;
    }


    /**
     * 3、创建容器
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
            operate = "container.create",
            service = "container_create")
    public String create(String body) {
        return null;
    }

    /**
     * 4、 操作容器
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/operation")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "container.operation",
            service = "container_operation")
    public String operationContainer(String body) {
        return null;
    }

    /**
     * 5、查询容器列表
     * <p>
     * <p>
     * private boolean count = false;  //是否count查询，如果true，只返回条数
     * private int start = 0; //分页offset start
     * private int limit = 10;//分页pagesize
     * private boolean replyWithCount = true;//返回结果是否包含总条数
     * private List<Conditions> conditions;////查询条件列表
     * private String sortBy = "";//排序字段
     * private String sortDirection = "asc"; //排序方向  asc:正序（默认）   desc ：倒序
     * <p>
     * <p>
     * String  s   true
     * <p>
     * <p>
     * <p>
     * {
     * "count": false,
     * "start": 1,
     * "limit": 2,
     * "replyWithCount": true,
     * "conditions": [
     * {
     * "name": "type",
     * "op": "=",
     * "value": "'DOCKER'"
     * }
     * ],
     * "sortBy": "name",
     * "s ortDirection": "asc"
     * }
     * <p>
     * <p>
     * <p>
     * {"count":false,"start":1,"limit":2,"replyWithCount":true,"conditions":[{"name":"type","op":"=","value":"'DOCKER'"}],"sortBy":"name","sortDirection":"asc"}
     * <p>
     * CommonQueryParam  的 json  格式字符串
     *
     * @retuen
     */
    @GET
    @Path("/getContainerList/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "container.info.list.query",
            service = "container_info_list_query"
    )
    public String getContainerList(@PathParam("s") String s) {
        return null;
    }

    /**
     * 6.批量创建容器
     */
    @PUT
    @Path("/batchCreate")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "container.batch.create",
            service = "container_batch_create"
    )
    public String batchCreate(String body) {
        return null;
    }


    /**
     * 7、容器更改计算方案
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/scheme/calculation/change")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "container.calculationScheme.change",
            service = "container_calculationScheme_change")
    public String  changeCalculationScheme(String body) {
        return null;
    }


    /**
     * 8、容器更改磁盘方案
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/scheme/disk/change")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "container.diskScheme.change",
            service = "container_diskScheme_change")
    public String changeDiskScheme(String body) {
        return null;
    }
}