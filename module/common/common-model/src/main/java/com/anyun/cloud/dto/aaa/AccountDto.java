/*
 *     Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package com.anyun.cloud.dto.aaa;

import java.util.Date;
import java.util.List;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/13/16
 */
public class AccountDto {
    private String id;
    private String name;
    private List<VerificationDto> verifications;
    private AccountTypeDto accountType;
    private CertificateDto certificate;
    private DomainDto domain;
    private List<RealmDto> realms;
    private int status = 1;

    private Date createDate;
    private Date lastModifyDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AccountTypeDto getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeDto accountType) {
        this.accountType = accountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public CertificateDto getCertificate() {
        return certificate;
    }

    public void setCertificate(CertificateDto certificate) {
        this.certificate = certificate;
    }

    public DomainDto getDomain() {
        return domain;
    }

    public void setDomain(DomainDto domain) {
        this.domain = domain;
    }

    public List<RealmDto> getRealms() {
        return realms;
    }

    public void setRealms(List<RealmDto> realms) {
        this.realms = realms;
    }

    public List<VerificationDto> getVerifications() {
        return verifications;
    }

    public void setVerifications(List<VerificationDto> verifications) {
        this.verifications = verifications;
    }
}
