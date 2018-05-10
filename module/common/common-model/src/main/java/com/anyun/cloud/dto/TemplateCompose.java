package com.anyun.cloud.dto;

import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.List;

/**
 * Created by sxt on 16-12-7.
 */
public class TemplateCompose    extends AbstractEntity {
    private List<TemplateContainer> templateContainers;
    private List<TemplateVolume> templateVolumes;
    private List<TemplateNet> templateNets;

    public List<TemplateContainer> getTemplateContainers() {
        return templateContainers;
    }

    public void setTemplateContainers(List<TemplateContainer> templateContainers) {
        this.templateContainers = templateContainers;
    }

    public List<TemplateVolume> getTemplateVolumes() {
        return templateVolumes;
    }

    public void setTemplateVolumes(List<TemplateVolume> templateVolumes) {
        this.templateVolumes = templateVolumes;
    }

    public List<TemplateNet> getTemplateNets() {
        return templateNets;
    }

    public void setTemplateNets(List<TemplateNet> templateNets) {
        this.templateNets = templateNets;
    }
}
