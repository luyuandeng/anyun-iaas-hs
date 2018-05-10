package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by twitchgg on 16-7-20.
 */
public class NetIpAddrInfoDto extends AbstractEntity{
    private String id ;
    private String ip;      // ip  地址
    private String label;   //  网络标签
    private int status = 0; // 0： 未分配   1：已分配    （默认 0）


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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
