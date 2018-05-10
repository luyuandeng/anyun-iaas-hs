package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sugs on 16-10-25.
 */
public class AddBridgeCommand extends ActionCommand implements BaseLinuxCommandExecuter{
    private static final Logger LOGGER = LoggerFactory.getLogger(AddBridgeCommand.class);

    private static String bridge;
    private static String network;

    public AddBridgeCommand(String bridge,String network){
        this.bridge = bridge;
        this.network = network;
    }

    @Override
    public Boolean exec() throws ApiException {
        //添加网桥
        String cmd = "ovs-vsctl add-br " + bridge;
        LOGGER.info("AddBridgeCommand's addBridge is " + cmd);
        String result = cmd(cmd);
        LOGGER.info("AddBridgeCommand's addbridge result is " + result);
        //绑定网卡
        cmd = "ovs-vsctl add-port " + bridge + " " + network;
        LOGGER.info("bind network's cmd is " + cmd);
        result = cmd(cmd);
        LOGGER.info("bind network's result is " + result);
        return true;
    }
}
