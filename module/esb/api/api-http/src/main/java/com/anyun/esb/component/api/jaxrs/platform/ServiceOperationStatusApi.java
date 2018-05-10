package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * 服务状态查询
 * Created by gp on 16-12-12.
 */
@Path("/services")
public class ServiceOperationStatusApi {
    /**
     * 1、查询所有服务列表
     * @return
     */
    @GET
    @Path("/allList")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component="anyun-host",
            operate ="query.service.operation.status",
            service="query_service_operation_status"
    )
    public String  getAllList(String userUniqueId){
        return  null;
    }

}
