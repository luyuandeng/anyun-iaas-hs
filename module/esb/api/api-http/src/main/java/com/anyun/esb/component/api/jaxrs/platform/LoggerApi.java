package com.anyun.esb.component.api.jaxrs.platform;

import com.anyun.esb.component.api.jaxrs.RestMethodDefine;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Created by gp on 16-11-2.
 */

@Path("/logger")
public class LoggerApi {
    /**
     * 1、
     * @param
     * @return
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    @RestMethodDefine(needAuthentication = true,
            component = "anyun-host",
            operate = "logger.query.by.condition",
            service = "logger_query_by_condition")
    public String getList(String ip,String starttime,String endtime,String grade,String keyword,String filename,int pagenum,int pagecount,String userUniqueId) {
        return null;
    }

}
