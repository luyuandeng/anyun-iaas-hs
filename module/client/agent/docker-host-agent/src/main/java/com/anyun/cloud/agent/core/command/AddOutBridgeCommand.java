package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import com.anyun.cloud.tools.bash.BashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sugs on 16-10-26.
 */
public class AddOutBridgeCommand implements BaseLinuxCommandExecuter{
    private static Logger LOGGER = LoggerFactory.getLogger(AddOutBridgeCommand.class);

    private String bridge;
    private String ip;

    public AddOutBridgeCommand(String bridge, String ip){
        this.bridge = bridge;
        this.ip = ip;
    }

    @Override
    public String exec() throws ApiException {
        String cmd = "ovs-ofctl add-flow " + bridge + " dl_type=0x0800,nw_src=" + ip + ",priority=100,action=normal";
        LOGGER.debug("cmd is " + cmd);
        BashCommand bashCommand = new BashCommand(cmd);
        String result = bashCommand.exec();
        if(bashCommand.getException() != null)
            throw new RuntimeException(bashCommand.getException());
        return result;
    }
}
