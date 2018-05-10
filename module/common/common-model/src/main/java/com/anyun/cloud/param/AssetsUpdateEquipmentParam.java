package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * Created by twh-workspace on 17-6-8.
 */
public class AssetsUpdateEquipmentParam extends AbstractEntity {
    private String serialNumber;//惨品序列号
    private String attribution;//惨品归属
    private String supplier;//供应商
    private String telephone;//联系电话
    private Date maintenancePeriod;//保修期

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
