package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * 安全组
 * Created by sxt on 16-10-17.
 */
@Path("/securityGroup")
public class SecurityGroupApi {

    /**
     * 1、根据label 查询安全组详情
     *
     * @param label
     * @param userUniqueId
     * @return SecurityGroupDto
     */
    @GET
    @Path("/details/{label}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "securityGroup.query.by.label",
            service = "securityGroup_query_by_label")
    public String getDetails(@PathParam("label") String label, String userUniqueId) {
        return null;
    }

    /**
     * 2、根据project 查询安全组列表
     *
     * @param project
     * @return SecurityGroupDto
     * @apram userUniqueId
     */
    @GET
    @Path("/list/{project}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "securityGroup.query.by.project",
            service = "securityGroup_query_by_project")
    public String getList(@PathParam("project") String project, String userUniqueId) {
        return null;
    }


    /**
     * 3、 根据label  删除 安全组
     *
     * @param label        安全组 ID
     * @param userUniqueId
     * @return Status<String>
     */
    @DELETE
    @Path("/delete/{label}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "securityGroup.delete.by.label",
            service = "securityGroup_delete_by_label")
    public String delete(@PathParam("label") String label, String userUniqueId) {
        return null;
    }

    /**
     * 4、创建一个安全组
     *
     * @param body
     * @return Status<String>
     */
    @PUT
    @Path("/create")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "securityGroup.create",
            service = "securityGroup_create")
    public String create(String body) {
        return null;
    }


    /**
     * 5、修改安全组
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/update")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "securityGroup.update",
            service = "securityGroup_update")
    public String update(String body) {
        return null;
    }


    /**
     * 6、 添加 容器Ip 到安全组
     *
     * @param
     * @return Status<String>
     */
    @POST
    @Path("/addIpToSecurityGroup")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "add.ip.to.securityGroup",
            service = "add_ip_to_securityGroup")
    public String addIpToSecurityGroup(String body) {
        return null;
    }


    /**
     * 7、移除 容器Ip 从安全组
     *
     * @param body
     * @return Status<String>
     */
    @POST
    @Path("/removeIpFromSecurityGroup")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "remove.ip.from.securityGroup",
            service = "remove_ip_from_securityGroup")
    public String removeIpFromSecurityGroup(String body) {
        return null;
    }

    /**
     * 8、根据条件查询安全组
     */
    @GET
    @Path("/conditions/{s}")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "SecurityGroup.query.by.conditions",
            service = "SecurityGroup_query_by_conditions"
    )
    public String getPageListConditions(@PathParam("s") String s) {
        return null;
    }


    /**
     * 9、查询 安全组 列表
     *
     * @return Lsit<SecurityGroupDto>
     * @apram userUniqueId  String  false  用户标识
     * @apram subMethod  String  true   子方法名称
     * QUERY_BY_PROJECTID：根据项目 ID  查询安全组列表表
     * projectId    String    true     主键
     * QUERY_BY_CONTAINERID ：根据容器 ID 查询安全组列表
     * containerId  String  true     主键
     * @apram subParameters  String  true  子参数   子方法对应的参数
     */
    @GET
    @Path("/list/query")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "securityGroup.list.query",
            service = "securityGroup_list_query")
    public String securityGroupQuery(String userUniqueId, String subMethod, String subParameters) {
        return null;
    }
}
