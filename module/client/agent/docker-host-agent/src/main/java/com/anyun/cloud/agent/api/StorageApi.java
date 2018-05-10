package com.anyun.cloud.agent.api;

import com.anyun.cloud.agent.common.Utils;
import com.anyun.cloud.agent.common.service.StorageService;
import com.anyun.cloud.agent.core.DefaultStorageService;
import com.anyun.cloud.agent.result.StorageInfo;
import com.anyun.cloud.api.Response;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by twitchgg on 16-8-3.
 */
@Path("storage")
public class StorageApi {
    private StorageService storageService;

    @Context
    private HttpServletRequest request;

    public StorageApi() {
        storageService = new DefaultStorageService();
    }

    @Path("list/{type}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String listStorageInfos(@PathParam("type") String type) {
        Response<List<StorageInfo>> response = storageService.listStorageByType(type);
        return Utils.toJson(response);
    }

    @Path("add/{type}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String addStorage(@PathParam("type") String type, String body) {
        Response<StorageInfo> response = storageService.addStorage(type, body);
        return Utils.toJson(response);
    }
}
