package com.anyun.cloud.agent.core.command;

import com.anyun.cloud.agent.common.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by sugs on 16-10-25.
 */
public class OvsReSetNetCommand extends ActionCommand implements BaseLinuxCommandExecuter{
    private static final Logger LOGGER = LoggerFactory.getLogger(OvsReSetNetCommand.class);

    private static String ip;  //IP
    private static String gw;   //网关
    private static String subnet;//子网
    private static String network;//网卡
    private static String netbridge;//网桥

    public OvsReSetNetCommand(String ip, String gw, String subnet, String network,String netbridge){
        this.ip = ip;
        this.gw = gw;
        this.subnet = subnet;
        this.network = network;
        this.netbridge = netbridge;
    }

    @Override
    public Boolean exec() throws ApiException {
        //重设网卡IP
        String cmd = "ifconfig " + network + "0.0.0.0";
        LOGGER.debug("ReSet network " + cmd);
        String result = cmd(cmd);
        LOGGER.debug("ReSet netWork's result is " + result);
        //设置网桥
        cmd = "ifconfig " + netbridge + " " + ip + "/" + subnet;
        result = cmd(cmd);
        LOGGER.debug("ReSet bridge's result is " + result);
        //设置默认网关
        cmd = "route add default gw " + gw;
        result = cmd(cmd);
        LOGGER.debug("ReSet gw's result is " + result);
        return true;
    }
}
