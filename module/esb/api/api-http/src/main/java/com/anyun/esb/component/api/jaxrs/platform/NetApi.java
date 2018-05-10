package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by sxt on 16-7-22.
 */
@Path("/net")
public class NetApi {
///////////////////////////////////////////////////网络标签///////////////////////////////////////////////////////////

    /**
     * 1、查询  网络标签详情
     *
     * @param userUniqueId String  false  用户标识
     * @param label        String  true     主键
     * @return NetLabelInfoDto>   网络标签信息
     */
    @GET
    @Path("/netLabelInfo/details/{label}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "netLabelInfo.query.by.label",
            service = "netLabelInfo_query_by_label")
    public String getNetLabelInfoDetails(@PathParam("label") String label,String userUniqueId) {
        return null;
    }


    /**
     * 2、查询  网络标签列表
     *
     * @param userUniqueId String  false  用户标识
     * @param type         String    true    有两种类型 openvswitch和bridge
     * @return List<NetLabelInfoDto>   网络标签信息列表 格式如   label
     */
    @GET
    @Path("/netLabelInfo/list/{type}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "netLabelInfo.query.by.type",
            service = "netLabelInfo_query_by_type")
    public String getNetLabelInfoList(String userUniqueId, @PathParam("type") String type) {
        return null;
    }


    /**
     * 3、查询 容器ip 列表
     *
     * @param userUniqueId  String   false   用户标识
     * @param subMethod     String   true    子方法
     *                      QUERY_BY_LABEL_CONTAINER：根据容器ID和网络标签 查询 容器IP信息
     *                      container     String    true     容器id
     *                      label         String    true     网络标签
     *                      QUERY_ALREADY_ADDED_BY_LABEL： 根据安全组标签 查询安全组中  已添加的
     *                      label   String   true    安全组标签
     *                      QUERY_NOT_ADD_BY_LABEL： 查询 未添加到该安全组的  容器ip 列表
     *                      label   String   true    安全组标签
     * @param subParameters 子参数  格式如  id|label
     * @return List<ContainerIpInfoDto>
     */
    @GET
    @Path("/containerIpInfo/list")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "containerIpInfo.list.query",
            service = "containerIpInfo_list_query")
    public String getContainerIpInfoList(String userUniqueId, String subMethod, String subParameters) {
        return null;
    }


    /**
     * 4、容器连上网络
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/containerConnectToNetwork")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "container.connect.ToNetwork",
            service = "container_connect_to_network")
    public String containerConnectToNetwork(String body) {
        return null;
    }


    /**
     * 5、容器断掉网络
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/containerDisconnectFromNetwork")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "container.disconnect.fromNetwork",
            service = "container_disconnect_from_network")
    public String containerDisconnectFromNetwork(String body) {
        return null;
    }



    /**
     * 6、查询容器ip列表
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
    @Path("/getContainerIpList/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "container.ip.info.list.query",
            service = "container_ip_info_list_query"
    )
    public String getContainerIpList(@PathParam("s" ) String  s) {
        return null;
    }


    /**
     * 7、根据条件分页查询L3网络
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
    @Path("/getL3NetList/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "netL3Info.list.query",
            service = "netL3Info_list_query"
    )
    public String getPageListConditions(@PathParam("s" ) String  s) {
        return null;
    }

}