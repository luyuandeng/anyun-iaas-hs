package com.anyun.cloud.agent.param;

/**
 * Created by sugs on 16-10-25.
 */
public class NetParam {

    /**
     * IP
     */
    private String ip;

    /**
     * 网关
     */
    private String gw;

    /**
     * 子网掩码
     */
    private String subnet;

    /**
     * 网卡
     */
    private String network;

    /**
     * 网桥
     * @return
     */
    private String netbridge;

    public String getNetbridge() {
        return netbridge;
    }

    public void setNetbridge(String netbridge) {
        this.netbridge = netbridge;
    }

    @Override
    public String toString() {
        return "NetParam{" +
                "ip='" + ip + '\'' +
                ", gw='" + gw + '\'' +
                ", subnet='" + subnet + '\'' +
                ", network='" + network + '\'' +
                ", netbridge='" + netbridge + '\'' +
                '}';
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getGw() {
        return gw;
    }

    public void setGw(String gw) {
        this.gw = gw;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
