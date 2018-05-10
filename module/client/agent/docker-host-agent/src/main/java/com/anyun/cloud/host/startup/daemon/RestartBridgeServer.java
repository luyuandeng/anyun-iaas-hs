package com.anyun.cloud.host.startup.daemon;

import com.anyun.cloud.tools.bash.BashCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sugs on 16-10-26.
 */
public class RestartBridgeServer {

    private static Logger LOGGER = LoggerFactory.getLogger(RestartBridgeServer.class);

    public static void restartBridge(){
        String cmd = "service openvswitch-switch restart";
        BashCommand bashCommand = new BashCommand(cmd);
        LOGGER.debug("restart ovs cmd is " + cmd);
        bashCommand.exec();
    }

    public static void delAllBridgeLimit(String bridge){
        String cmd = "ovs-ofctl del-flows " + bridge + " ip";
        BashCommand bashCommand = new BashCommand(cmd);
        bashCommand.exec();
    }
}
