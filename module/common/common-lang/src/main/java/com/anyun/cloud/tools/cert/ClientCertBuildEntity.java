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

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

/**
 * Created by mr-tan on 16-3-9.
 */
public class ClientCertBuildEntity {

    public static String DEFAULT_SIGNATUREALGORITHM = "SHA1WithRSA";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    private String issuer;

    private Date notBeforeDate;

    private Date notAfterDate;

    private String subject;

    private String signatureAlgorithm = DEFAULT_SIGNATUREALGORITHM;

    public ClientCertBuildEntity(CertEntity certEntity, PrivateKey privateKey,
                                 PublicKey publicKey, String issuer){

        this.privateKey = privateKey;

        this.publicKey = publicKey;

        this.issuer = issuer;

    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public Date getNotBeforeDate() {

        if(notBeforeDate == null){
            return new Date(System.currentTimeMillis()-1000L*60*60*24*30);
        }
        return notBeforeDate;
    }

    public void setNotBeforeDate(Date notBeforeDate) {
        this.notBeforeDate = notBeforeDate;
    }

    public Date getNotAfterDate() {

        if(notAfterDate == null){

            return new Date(System.currentTimeMillis()+(1000L*60*60*24*30));
        }
        return notAfterDate;
    }

    public void setNotAfterDate(Date notAfterDate) {
        this.notAfterDate = notAfterDate;
    }

    public String getSignatureAlgorithm() {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
