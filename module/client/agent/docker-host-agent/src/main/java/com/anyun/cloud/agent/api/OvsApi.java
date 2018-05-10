package com.anyun.cloud.agent.api;

import com.anyun.cloud.agent.common.Utils;
import com.anyun.cloud.agent.common.service.OvsService;
import com.anyun.cloud.agent.core.DefaultOvsService;
import com.anyun.cloud.agent.param.AddOutBridgeParam;
import com.anyun.cloud.agent.param.NetParam;
import com.anyun.cloud.api.Response;
import com.anyun.cloud.api.Status;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by sugs on 16-10-25.
 */

@Path("ovs")
public class OvsApi {

    private OvsService ovsService = new DefaultOvsService();

    /**
     * 重置网络
     * @param param
     * @return
     */
    @Path("reSetNet")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String ResSetNet(NetParam param){
        Response<Status<Boolean>> response = ovsService.ResetNet(param);
        return Utils.toJson(response);
    }

    /**
     * 添加网桥
     */
    @Path("addBridge")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String addBridge(NetParam param){
        Response<Status<Boolean>> response = ovsService.addBridge(param);
        return Utils.toJson(response);
    }

    /**
     * 删除网桥
     */
    @Path("delBridge")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public String delBridge(NetParam param){
        Response<Status<Boolean>> response = ovsService.delBridge(param);
        return Utils.toJson(response);
    }

    /**
     * 初始化全局控制
     */
    @Path("addAllBridge")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String addAllBridge(String bridge){
        Response<Status<String>> response = ovsService.addAllBridge(bridge);
        return Utils.toJson(response);
    }

    /**
     * 初始化出权限
     */
    @Path("addOutBridge")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String addOutBridge(AddOutBridgeParam addOutBridgeParam){
        Response<Status<String>> response = ovsService.addOutBridge(addOutBridgeParam);
        return Utils.toJson(response);
    }

    /**
     * 添加限制
     */
    @Path("addVisitLimit/{bridge}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String addVisitLimit(@PathParam("bridge")String bridge,String list){
        Response<Status<Boolean>> response = ovsService.addVisitLimit("br-anyun",list);
        return Utils.toJson(response);
    }
}
