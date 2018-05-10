package com.anyun.cloud.param;

import com.anyun.cloud.tools.db.AbstractEntity;

public class ContainerChangeCalculationSchemeParam extends AbstractEntity {
    /**
     * 容器id
     */
    private String id;
    /**
     * 计算方案id
     */
    private String calculationSchemeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCalculationSchemeId() {
        return calculationSchemeId;
    }

    public void setCalculationSchemeId(String calculationSchemeId) {
        this.calculationSchemeId = calculationSchemeId;
    }
}
