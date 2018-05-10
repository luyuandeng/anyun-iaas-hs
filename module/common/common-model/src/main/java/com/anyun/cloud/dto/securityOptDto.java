package com.anyun.cloud.dto;


/**
 * Created by hwt on 16-6-16.
 */
public class securityOptDto {

    private String user;
    private String role;
    private String type;
    private String level;
    private String disable;
    private String profile;
    private String noNewPricileges;
    private String unconfined;
    private String profileJson;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDisable() {
        return disable;
    }

    public void setDisable(String disable) {
        this.disable = disable;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getNoNewPricileges() {
        return noNewPricileges;
    }

    public void setNoNewPricileges(String noNewPricileges) {
        this.noNewPricileges = noNewPricileges;
    }

    public String getUnconfined() {
        return unconfined;
    }

    public void setUnconfined(String unconfined) {
        this.unconfined = unconfined;
    }

    public String getProfileJson() {
        return profileJson;
    }

    public void setProfileJson(String profileJson) {
        this.profileJson = profileJson;
    }
}
