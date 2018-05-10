package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sugs on 16-10-25.
 */
public class DeleteBridgeCommand extends ActionCommand implements BaseLinuxCommandExecuter{
    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteBridgeCommand.class);

    private static String bridge;

    public DeleteBridgeCommand(String bridge){
        this.bridge = bridge;
    }

    @Override
    public Boolean exec() throws ApiException {
        String cmd = "ovs-vsctl del-br " + bridge;
        LOGGER.info("deleteBridge's cmd is " + cmd);
        String result = cmd(cmd);
        LOGGER.info("deleteBridge's result is " + result);
        return true;
    }
}
