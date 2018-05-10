package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by gp on 16-9-21.
 */
public class NetMonitorDto extends AbstractEntity {
    private String name;  //网卡名称
    private String rxBytes;// 接收流量
    private String txBytes; // 发送流量

    @Override
    public String toString() {
        return "NetMonitorDto{" +
                "name='" + name + '\'' +
                ", rxBytes='" + rxBytes + '\'' +
                ", txBytes='" + txBytes + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRxBytes() {
        return rxBytes;
    }

    public void setRxBytes(String rxBytes) {
        this.rxBytes = rxBytes;
    }

    public String getTxBytes() {
        return txBytes;
    }

    public void setTxBytes(String txBytes) {
        this.txBytes = txBytes;
    }
}
