package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 16-9-12.
 */
public class HostAndIpDto extends AbstractEntity {
    private String host;  // 宿主机 序列号
    private String ip;    // 宿主机 管理 ip

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
