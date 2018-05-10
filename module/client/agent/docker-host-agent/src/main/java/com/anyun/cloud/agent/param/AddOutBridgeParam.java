package com.anyun.cloud.agent.param;

/**
 * Created by sugs on 16-10-26.
 */
public class AddOutBridgeParam {

    private String bridge;
    private String ip;

    @Override
    public String toString() {
        return "AddOutBridgeParam{" +
                "bridge='" + bridge + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

    public String getBridge() {
        return bridge;
    }

    public void setBridge(String bridge) {
        this.bridge = bridge;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
