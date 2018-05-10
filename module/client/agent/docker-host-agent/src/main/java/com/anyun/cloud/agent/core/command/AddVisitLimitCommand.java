package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.agent.param.OvsBridgeParam;
import com.anyun.cloud.tools.bash.BashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sugs on 16-10-26.
 */
public class AddVisitLimitCommand extends ActionCommand implements BaseLinuxCommandExecuter{
    private static Logger LOGGER = LoggerFactory.getLogger(AddVisitLimitCommand.class);

    private static int[] nw_protos = {6,17};
    private String result;

    private String bridge;
    private String priority;        //权重
    private String nw_src;          //源IP
    private String nw_dst;          //目标IP
    private int tp_dst;             //端口
    private int nw_proto;        //TCP/UDP
    private String action;          //操作

    public AddVisitLimitCommand(OvsBridgeParam ovsBridgeParam){
        this.priority = ovsBridgeParam.getPriority();
        this.nw_src = ovsBridgeParam.getNw_src();
        this.nw_dst = ovsBridgeParam.getNw_dst();
        this.tp_dst = ovsBridgeParam.getTp_dst();
        this.nw_proto = ovsBridgeParam.getNw_proto();
        this.action = ovsBridgeParam.getAction();
        this.bridge = ovsBridgeParam.getBridge();
    }

    @Override
    public String exec() throws ApiException {
        String[] params = {priority,action,nw_dst};
        String cmd = "ovs-ofctl add-flow " + bridge + " dl_type=0x0800,nw_proto=" + nw_proto + ",tp_dst=" + tp_dst
                + ",nw_dst=" + nw_dst + ",priority=" + priority + ",action=" + action;
        BashCommand bashCommand = new BashCommand(cmd);
        bashCommand.exec();
        if(bashCommand.getException() != null){
            LOGGER.debug("addVisitLimitCommand get error " + bashCommand.getException());
            throw new RuntimeException(bashCommand.getException());
        }
        return result;
    }

    public Boolean exits(String[] params){
        for(String param : params){
            if(param.isEmpty() || param == null){
                result = param + " is empty or null";
                return false;
            }
        }
        result = "success";
        return true;
    }
}
