package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.tools.bash.BashCommand;

/**
 * Created by sugs on 16-10-25.
 */
public class ActionCommand {
    public String cmd(String cmd){
        BashCommand bashCommand = new BashCommand(cmd);
        String result = bashCommand.exec();
        if(bashCommand.getException() != null)
            throw new RuntimeException(bashCommand.getException());
        return result;
    }

    public Boolean bridge_exists(String bridge){
        String cmd = "ovs-vsctl br-exists " + bridge;
        BashCommand bashCommand = new BashCommand(cmd);
        bashCommand.exec();
        if(bashCommand.getResult().getExitValue() != 0)
            return false;
        return true;
    }
}
