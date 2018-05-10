package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * Created by sxt on 16-7-21.
 */
public class NetLabelInfoDto extends AbstractEntity {
    private   String   label ;//网络标签主键
    private   String   name  ;//网络标签名称
    private   String   descr ;//网络标签描述
    private   String   gateway;//网关
    private   String   subnet;//网段及掩码
    private   String   type  ; //网络类型：bridge  openvswitch
    private   Date     createtime;//创建时间

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
