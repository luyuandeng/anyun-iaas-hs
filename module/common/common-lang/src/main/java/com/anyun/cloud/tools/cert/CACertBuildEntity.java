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

package com.anyun.cloud.tools.cert;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * Created by mr-tan on 16-3-9.
 */
public class CACertBuildEntity {
    public static String DEFAULT_SIGNATUREALGROITHM = "SHA1WithRSAEncryption";
    private BigInteger serialNumber;
    private PrivateKey caPrivateKey;
    private PublicKey caPublicKey;
    private String issuer;
    private String subject;
    private Date notBeforeDate;
    private Date notAfterDate;
    private Date checkValidityDate;
    private String signatureAlgorithm = DEFAULT_SIGNATUREALGROITHM;

    public CACertBuildEntity(BigInteger serialNumber, PrivateKey caPrivateKey,
                             PublicKey caPublicKey, String issuer, String subject) {
        this.serialNumber = serialNumber;
        this.caPrivateKey = caPrivateKey;
        this.caPublicKey = caPublicKey;
        this.issuer = issuer;
        this.subject = subject;

    }

    public BigInteger getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(BigInteger serialNumber) {
        this.serialNumber = serialNumber;
    }

    public PublicKey getCaPublicKey() {
        return caPublicKey;
    }

    public void setCaPublicKey(PublicKey caPublicKey) {
        this.caPublicKey = caPublicKey;
    }

    public PrivateKey getCaPrivateKey() {
        return caPrivateKey;
    }

    public void setCaPrivateKey(PrivateKey caPrivateKey) {
        this.caPrivateKey = caPrivateKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getNotBeforeDate() {
        if (notBeforeDate == null) {
            return new Date(System.currentTimeMillis());
        }
        return notBeforeDate;
    }

    public void setNotBeforeDate(Date notBeforeDate) {
        this.notBeforeDate = notBeforeDate;
    }

    public Date getNotAfterDate() {
        if (notAfterDate == null) {
            return new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365 * 10));
        }
        return notAfterDate;
    }

    public void setNotAfterDate(Date notAfterDate) {
        this.notAfterDate = notAfterDate;
    }

    public Date getCheckValidityDate() {
        if (checkValidityDate == null) {
            return new Date();
        }
        return checkValidityDate;
    }

    public void setCheckValidityDate(Date checkValidityDate) {
        this.checkValidityDate = checkValidityDate;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }
}
