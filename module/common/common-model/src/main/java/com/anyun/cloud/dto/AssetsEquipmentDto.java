package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * Created by sxt on 17-4-7.
 */
public class AssetsEquipmentDto extends AbstractEntity {
    private String serialNumber;  //产品序列号
    private String attribution;// 产品归属
    private String supplier;// 供应商
    private String telephone;//联系电话
    private Date maintenancePeriod; // 产品保修期

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAttribution() {
        return attribution;
    }

    public void setAttribution(String attribution) {
        this.attribution = attribution;
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

    public Date getMaintenancePeriod() {
        return maintenancePeriod;
    }

    public void setMaintenancePeriod(Date maintenancePeriod) {
        this.maintenancePeriod = maintenancePeriod;
    }
}
