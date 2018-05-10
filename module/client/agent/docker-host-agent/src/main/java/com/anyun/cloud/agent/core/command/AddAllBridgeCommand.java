package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sugs on 16-10-26.
 */
public class AddAllBridgeCommand extends ActionCommand implements BaseLinuxCommandExecuter{

    private static Logger LOGGER = LoggerFactory.getLogger(AddAllBridgeCommand.class);

    private String bridge;

    public AddAllBridgeCommand(String bridge){
        this.bridge = bridge;
    }

    @Override
    public String exec() throws ApiException {
        if(bridge_exists(bridge)){
            String cmd = "ovs-ofctl add-flow " + bridge + " dl_type=0x0800,priority=9,action=drop";
            LOGGER.info("cmd is " + cmd);
            cmd(cmd);
            return "success";
        }
        return "bridge " + bridge + " not exist";
    }
}
