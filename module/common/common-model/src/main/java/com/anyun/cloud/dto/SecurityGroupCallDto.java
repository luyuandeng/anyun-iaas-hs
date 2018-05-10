package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by gp on 16-10-28.
 */
public class SecurityGroupCallDto extends AbstractEntity {
    private String hostip;
    private String containerIp;
    private String port;
    private String ipOrSegment;

    public String getHostip() {
        return hostip;
    }

    public void setHostip(String hostip) {
        this.hostip = hostip;
    }

    public String getContainerIp() {
        return containerIp;
    }

    public void setContainerIp(String containerIp) {
        this.containerIp = containerIp;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getIpOrSegment() {
        return ipOrSegment;
    }

    public void setIpOrSegment(String ipOrSegment) {
        this.ipOrSegment = ipOrSegment;
    }
}
