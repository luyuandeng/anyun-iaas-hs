package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

public class AssetsUpdateParam extends AbstractEntity {
    /**
     * 主键
     */
    private String id;

    /**
     * 设备是否使用
     * 1  已使用
     * 0 未使用
     */
    private int used;


    /**
     * 用途
     * 例如 Docker Server
     */
    private String purpose;


    /**
     * 显示名称
     */
    private String displayName;


    /**
     * 设备名称
     * 例如Docker01
     */
    private String deviceName;


    /**
     * 设备序列号
     */
    private String deviceSerialNumber;


    /**
     * 设备型号
     */
    private String deviceModel;

//
//    /**
//     * 设备分类
//     * SERVER ：服务器
//     * ROUTER ：路由器
//     * SWITCH ：交换机
//     * OTHER  ：其他设备
//     */
//    private String deviceCategory;


    /**
     * 分类描述
     * 例如 虚拟化应用服务器
     */
    private String categoryDescription;


    /**
     * 管理ip （设备分类：交换机或路由器等网络设备 才有值）
     */
    private String managementIp;

    /**
     * 系统ip （设备分类 是 交换机或路由器等网络设备 才有值）
     * 例如 15.16.0.13
     */
    private String systemIp;

    /**
     * IPMI ip （设备分类 是 服务器时 才有值）
     * 例如 172.16.1.13
     */
    private String ipmiIp;

    /**
     * 物理位置
     */
    private String position;

    /**
     * cpu （设备分类 是 服务器时 才有值）
     * 例如 4*E7 4870
     */
    private String cpu;

    /**
     * 内存 （设备分类 是 服务器时 才有值）
     * 例如 256GB
     */
    private String memory;

    /**
     * 硬盘 （设备分类 是 服务器时 才有值）
     * 1*400G SSD，9*3TB
     */
    private String hardDisk;

    /**
     * 设备归属
     */
    private String deviceBelong;

    /**
     * 供应商
     */
    private String supplier;

    /**
     * 联系电话
     */
    private String telephone;

    /**
     * 保修期
     */
    private String maintenancePeriod;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUsed() {
        return used;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(String deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
//
//    public String getDeviceCategory() {
//        return deviceCategory;
//    }
//
//    public void setDeviceCategory(String deviceCategory) {
//        this.deviceCategory = deviceCategory;
//    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public void setCategoryDescription(String categoryDescription) {
        this.categoryDescription = categoryDescription;
    }

    public String getManagementIp() {
        return managementIp;
    }

    public void setManagementIp(String managementIp) {
        this.managementIp = managementIp;
    }

    public String getSystemIp() {
        return systemIp;
    }

    public void setSystemIp(String systemIp) {
        this.systemIp = systemIp;
    }

    public String getIpmiIp() {
        return ipmiIp;
    }

    public void setIpmiIp(String ipmiIp) {
        this.ipmiIp = ipmiIp;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getHardDisk() {
        return hardDisk;
    }

    public void setHardDisk(String hardDisk) {
        this.hardDisk = hardDisk;
    }

    public String getDeviceBelong() {
        return deviceBelong;
    }

    public void setDeviceBelong(String deviceBelong) {
        this.deviceBelong = deviceBelong;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMaintenancePeriod() {
        return maintenancePeriod;
    }

    public void setMaintenancePeriod(String maintenancePeriod) {
        this.maintenancePeriod = maintenancePeriod;
    }
}
