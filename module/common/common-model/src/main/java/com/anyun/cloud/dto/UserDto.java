/*
 *
 *      UserDto.java
 *      Copyright (C) <2015-?>  <twitchgg@yahoo.com>
 *
 *      This program is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.anyun.cloud.dto;

import com.anyun.cloud.api.account.Organization;
import com.anyun.cloud.tools.db.AbstractEntity;

import java.util.Date;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/17/16
 */
public class UserDto extends AbstractEntity{
    private String id;  //用户ID
    private String keySeialNumber;  //用户证书序列号
    private int loginMethod;    //登录方法 0证书密码登录,1证书登录,2密码登录
    private String loginName;   //登录名称
    private String passwdEncrypt;   //加密的密码
    private String passwdEncryptType = "MD5";   //加密类型
    private String realName;    //真实姓名
    private String mail;    //用户邮箱
    private Date createDate;    //创建时间
    private Date lastModifyDate;    //最后修改时间
    private Date lastLoginDate; //最后登录时间
    private int status; //用户状态 0禁用,1正常
    private OrganizationDto organization = new OrganizationDto();  //用户所属组织

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public OrganizationDto getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDto organization) {
        this.organization = organization;
    }

    public String getPasswdEncrypt() {
        return passwdEncrypt;
    }

    public void setPasswdEncrypt(String passwdEncrypt) {
        this.passwdEncrypt = passwdEncrypt;
    }

    public String getKeySeialNumber() {
        return keySeialNumber;
    }

    public void setKeySeialNumber(String keySeialNumber) {
        this.keySeialNumber = keySeialNumber;
    }

    public int getLoginMethod() {
        return loginMethod;
    }

    public void setLoginMethod(int loginMethod) {
        this.loginMethod = loginMethod;
    }

    public String getPasswdEncryptType() {
        return passwdEncryptType;
    }

    public void setPasswdEncryptType(String passwdEncryptType) {
        this.passwdEncryptType = passwdEncryptType;
    }
}
