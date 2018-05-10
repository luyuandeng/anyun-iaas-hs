package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

/**
 * Created by sxt on 17-3-28.
 */
public class HostNetworkCardDto extends AbstractEntity {
    /**
     * 网卡bond是通过把多张网卡绑定为一个逻辑网卡，实现本地网卡的冗余，带宽扩容和负载均衡。在应用部署中是一种常用的技术，我们公司基本所有的项目相关服务器都做了bond,这里总结整理，以便待查。
     * Mode=0(balance-rr) 表示负载分担round-robin，和交换机的聚合强制不协商的方式配合。
     * Mode=1(active-backup) 表示主备模式，只有一块网卡是active,另外一块是备的standby，这时如果交换机配的是捆绑，将不能正常工作，因为交换机往两块网卡发包，有一半包是丢弃的。
     * Mode=2(balance-xor) 表示XOR Hash负载分担，和交换机的聚合强制不协商方式配合。（需要xmit_hash_policy）
     * Mode=3(broadcast) 表示所有包从所有interface发出，这个不均衡，只有冗余机制...和交换机的聚合强制不协商方式配合。
     * Mode=4(802.3ad) 表示支持802.3ad协议，和交换机的聚合LACP方式配合（需要xmit_hash_policy）
     * Mode=5(balance-tlb) 是根据每个slave的负载情况选择slave进行发送，接收时使用当前轮到的slave
     * Mode=6(balance-alb) 在5的tlb基础上增加了rlb。
     */

    private String id;// 主键
    private String host;  //宿主机 Id
    private String name; // 网卡名称
    private String description; //网卡描述
    private String product; //网卡型号
    private String vendor; //供应商
    private String mac; //mac 地址
    private String speed; //速度
    private String model; //网卡绑定模式  七种   0,1,2,3,4,5,6
    private String bondDevice; //Bound 设备

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBondDevice() {
        return bondDevice;
    }

    public void setBondDevice(String bondDevice) {
        this.bondDevice = bondDevice;
    }
}
