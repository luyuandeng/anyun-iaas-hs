package com.anyun.cloud.dto;

/**
 * Created by gaopeng on 16-8-8.
 */
public class ContainerIpInfoDto {
    private String id;//容器ip地址
    private String ip;//分配給容器的ip地址
    private String mac;//mac地址
    private String label;//网络标签id
    private String container;//容器id

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

}
