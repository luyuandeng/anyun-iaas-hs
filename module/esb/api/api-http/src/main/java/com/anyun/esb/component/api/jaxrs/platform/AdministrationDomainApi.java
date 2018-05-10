package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.*;

/**
 * Created by gp on 17-2-22.
 */
@Path("/domain")
public class AdministrationDomainApi {
    /**
     * 根据条件创建管理域
     * @param body
     * @return
     */
    @PUT
    @Path("/createByCondition")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "AdministrationDomain.create.by.condition",
            service = "AdministrationDomain_create_by_condition")
    public String createAdministrationDomainByCondition(String body) {return null;}


    /**
     * 根据ID删除管理域
     * @param id
     * @return
     */
    @DELETE
    @Path("/deleteById")
    @Produces("application/json")
    @Consumes("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "AdministrationDomain.delete.by.id",
            service = "AdministrationDomain_delete_by_id")
    public String deleteAdministrationDomainById(String id){return null;}

}
