package com.anyun.cloud.agent.core;

import com.anyun.cloud.agent.common.service.OvsService;
import com.anyun.cloud.agent.core.command.*;
import com.anyun.cloud.agent.param.AddOutBridgeParam;
import com.anyun.cloud.agent.param.NetParam;
import com.anyun.cloud.agent.param.OvsBridgeParam;
import com.anyun.cloud.api.Response;
import com.anyun.cloud.api.Status;
import com.anyun.cloud.host.startup.daemon.RestartBridgeServer;
import com.anyun.cloud.tools.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by sugs on 16-10-25.
 */
public class DefaultOvsService implements OvsService{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOvsService.class);

    /**
     * 重置网络
     * @param netParam
     * @return
     */
    @Override
    public Response<Status<Boolean>> ResetNet(NetParam netParam) {
        OvsReSetNetCommand ovsCommand = new OvsReSetNetCommand(netParam.getIp(),netParam.getGw(),netParam.getSubnet(),netParam.getNetwork(),netParam.getNetbridge());
        Response<Status<Boolean>> response = new Response<>();
        response.setType("ResetNet");

        try {
            Boolean bool = ovsCommand.exec();
            response.setContent(new Status<>(bool));
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.debug(e.getMessage());
        }
        return response;
    }

    /**
     * 添加网桥
     * @return
     */
    @Override
    public Response<Status<Boolean>> addBridge(NetParam netParam) {
        AddBridgeCommand addBridgeCommand = new AddBridgeCommand(netParam.getNetbridge(),netParam.getNetwork());
        Response<Status<Boolean>> response = new Response<>();
        response.setType("addBridge");

        try {
            Boolean bool = addBridgeCommand.exec();
            response.setContent(new Status<>(bool));
        } catch (Exception e){
            e.printStackTrace();
            LOGGER.debug(e.getMessage());
        }
        return response;
    }

    /**
     * 删除网桥
     * @return
     */
    @Override
    public Response<Status<Boolean>> delBridge(NetParam netparam) {
        DeleteBridgeCommand deleteBridgeCommand = new DeleteBridgeCommand(netparam.getNetbridge());
        Response<Status<Boolean>> response = new Response<>();
        response.setType("delBridge");

        try{
            Boolean bool = deleteBridgeCommand.exec();
            response.setContent(new Status<>(bool));
        } catch (Exception e){
            LOGGER.debug(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }


    @Override
    public Response<Status<String>> addAllBridge(String bridge) {
        AddAllBridgeCommand addAllBridgeCommand = new AddAllBridgeCommand(bridge);
        Response<Status<String>> response = new Response<>();
        response.setType("addAllBridge");
        try{
            String result = addAllBridgeCommand.exec();
            response.setContent(new Status<>(result));
        } catch (Exception e){
            LOGGER.debug(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public Response<Status<String>> addOutBridge(AddOutBridgeParam addOutBridgeParam) {
        AddOutBridgeCommand addOutBridgeCommand = new AddOutBridgeCommand(addOutBridgeParam.getBridge(),addOutBridgeParam.getIp());
        Response<Status<String>> response = new Response<>();
        response.setType("addOutBridge");
        try{
            String result = addOutBridgeCommand.exec();
            response.setContent(new Status<>(result));
        } catch (Exception e){
            LOGGER.debug(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }


    @Override
    public Response<Status<Boolean>> addVisitLimit(String bridge,String list) {

        Map<String,Object> map = JsonUtil.fromJson(Map.class,list);

        List<Map> ovs = (List<Map>) map.get("params");

        RestartBridgeServer.delAllBridgeLimit(bridge);  //删除所有添加的限制

        AddAllBridgeCommand addAllBridgeCommand = new AddAllBridgeCommand(bridge);
        try{
            addAllBridgeCommand.exec();               //添加全局控制
        } catch (Exception e){
            LOGGER.debug(e.getMessage());
            e.printStackTrace();
        }

        Response<Status<Boolean>> response = new Response<>();
        response.setType("addVisitLimit");
        if(ovs != null){
            AddOutBridgeCommand addOutBridgeCommand = new AddOutBridgeCommand(bridge,ovs.get(0).get("nw_dst").toString());
            addOutBridgeCommand.exec();
        }

        for(Map map1 : ovs){
            OvsBridgeParam ovsBridgeParam = new OvsBridgeParam();
            ovsBridgeParam.setAction(map1.get("action").toString());
            ovsBridgeParam.setNw_dst(map1.get("nw_dst").toString());
            ovsBridgeParam.setNw_proto(Integer.parseInt(map1.get("nw_proto").toString().substring(0,map1.get("nw_proto").toString().indexOf("."))));
            ovsBridgeParam.setBridge(bridge);
            ovsBridgeParam.setPriority(map1.get("priority").toString());
            ovsBridgeParam.setTp_dst(Integer.parseInt(map1.get("tp_dst").toString().substring(0,map1.get("tp_dst").toString().indexOf("."))));
            AddVisitLimitCommand addVisitLimitCommand = new AddVisitLimitCommand(ovsBridgeParam);
            addVisitLimitCommand.exec();
        }
        response.setContent(new Status<>(true));
        return response;
    }

    @Override
    public Response<Status<Boolean>> delVisitLimit() {
        return null;
    }
}
