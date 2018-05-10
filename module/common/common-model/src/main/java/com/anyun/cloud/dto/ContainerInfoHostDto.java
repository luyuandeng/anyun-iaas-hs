package com.anyun.cloud.dto;

/**
 * Created by sxt on 16-7-7.
 */
public class ContainerInfoHostDto  {
    private  String  container;//容器id
    private  String  host;//宿主机
    private  String  ip;//分配給容器的ip地址

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
