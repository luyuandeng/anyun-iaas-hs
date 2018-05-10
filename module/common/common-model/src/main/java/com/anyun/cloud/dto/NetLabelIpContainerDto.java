package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-8-17.
 */
public class NetLabelIpContainerDto extends AbstractEntity {
    private String container;// 容器id
    private String containerName;// 容器純、名稱
    private String netLabel;////网络标签主键
    private String netLabelName;////网络标签名称
    private String gateway;
    private String subnet;
    private String ip;
    private String mac;

    @Override
    public String toString() {
        return "NetLabelIpContainerDto{" +
                "container='" + container + '\'' +
                ", containerName='" + containerName + '\'' +
                ", netLabel='" + netLabel + '\'' +
                ", netLabelName='" + netLabelName + '\'' +
                ", gateway='" + gateway + '\'' +
                ", subnet='" + subnet + '\'' +
                ", ip='" + ip + '\'' +
                ", mac='" + mac + '\'' +
                '}';
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getNetLabel() {
        return netLabel;
    }

    public void setNetLabel(String netLabel) {
        this.netLabel = netLabel;
    }

    public String getNetLabelName() {
        return netLabelName;
    }

    public void setNetLabelName(String netLabelName) {
        this.netLabelName = netLabelName;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getSubnet() {
        return subnet;
    }

    public void setSubnet(String subnet) {
        this.subnet = subnet;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
