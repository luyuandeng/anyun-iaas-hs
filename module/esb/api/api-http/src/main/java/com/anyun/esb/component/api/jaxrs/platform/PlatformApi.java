package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by sxt on 16-10-13.
 */
@Path("/platform")
public class PlatformApi {
    /**
     * @param body
     * @return Status<String>
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "platform.create",
            service = "platform_create")
    public String platformCreate(String body) {
        return null;
    }

    /**
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "platform.update",
            service = "platform_update")
    public String platformUpdate(String body) {
        return null;
    }


    /**
     * @param  body
     * @return Status<String>
     */
    @POST
    @Path("/setAsDefault")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "platform.set.as.default",
            service = "platform_set_as_default")
    public String platformSetAsDefault(String body) {return null;}

    /**
     * @param id
     * @param userUniqueId
     * @return Status<String>
     */
    @DELETE
    @Path("/delete")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "platform.delete",
            service = "platform_delete")
    public String platformDelete(String id,String userUniqueId) {
        return null;
    }



    /**
     * 查询 逻辑中心 列表
     *
     * @param userUniqueId  String  false  用户标识
     * @param subMethod     String  true   子方法名称
     *                      QUERY_BY_ID: 根据 Id 查询  逻辑中心
     *                      id   String    true     主键
     *                      QUERY_A_DEFAULT:  查询一个 默认的逻辑中心
     *                      QUERY_ALL:   查询所有的逻辑中心
     * @param subParameters String  true  子参数   子方法对应的参数 格式为 p1|p2|p3 .......
     * @return List<PlatformDto>
     */
    @GET
    @Path("/logicCenterQuery")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "logicCenter.query",
            service = "logicCenter_query")
    public String logicCenterQuery(String userUniqueId, String subMethod, String subParameters) {
        return null;
    }
    /************************************************************************合并之前的API********************************************************************/

    /**
     * @param id
     * @param userUniqueId
     * @return PlatformDto
     */
    @GET
    @Path("/queryById")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "platform.query.by.id",
            service = "platform_query_by_id")
    public String platformQueryById(String id,String userUniqueId) {
        return null;
    }


    /**
     * @param userUniqueId
     * @return PlatformDto
     */
    @GET
    @Path("/queryDefault")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "platform.query.default",
            service = "platform_query_default"
    )
    public String   platformQueryDefault(String userUniqueId) {
        return null;
    }

    /**
     * @param  userUniqueId
     * @return List<PlatformDto>
     */
    @GET
    @Path("/queryAll")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "platform.query.all",
            service = "platform_query_all"
    )
    public String platformQueryAll(String userUniqueId) {
        return null;
    }
}
