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
import java.security.cert.X509Certificate;

/**
 * Created by mr-tan on 16-3-9.
 */
public class CertEntity {

    private X509Certificate x509Certificate;

    private PrivateKey privateKey;

    private PublicKey publicKey;

    public CertEntity(X509Certificate x509Certificate, PrivateKey privateKey, PublicKey publicKey){

        this.x509Certificate = x509Certificate;

        this.privateKey = privateKey;

        this.publicKey = publicKey;
    }

    public X509Certificate getX509Certificate() {
        return x509Certificate;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
