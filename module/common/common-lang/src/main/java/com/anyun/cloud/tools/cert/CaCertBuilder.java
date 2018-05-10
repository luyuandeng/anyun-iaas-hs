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

import org.bouncycastle.crypto.prng.FixedSecureRandom;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.math.BigInteger;
import java.security.*;

/**
 * Created by mr-tan on 16-3-10.
 */
public class CaCertBuilder {

    public CaCertBuilder() {

    }

    public CertEntity buildLambdasoftDefaultCA(String x500Principal) throws Exception {
        String issuer = x500Principal;
        String subject = x500Principal;
        PrivateKey privateKey = null;
        PublicKey publicKey = null;
        KeyPair keyPair = genRSAKey();
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        CACertBuildEntity caCertBuildEntity = new CACertBuildEntity(
                BigInteger.valueOf(System.nanoTime()), privateKey, publicKey, issuer, subject);
        CertBuilder certBuilder = LambdasoftPKIProvider.getProvider().getCertBuilder();
        return new CertEntity(certBuilder.buildAcIssuerCert(caCertBuildEntity), privateKey, publicKey);
    }

    public CertEntity buildLambdasoftDefaultCA(String x500Principal,KeyPair keyPair) throws Exception {
        String issuer = x500Principal;
        String subject = x500Principal;
        PrivateKey privateKey = null;
        PublicKey publicKey = null;
        publicKey = keyPair.getPublic();
        privateKey = keyPair.getPrivate();
        CACertBuildEntity caCertBuildEntity = new CACertBuildEntity(
                BigInteger.valueOf(System.nanoTime()), privateKey, publicKey, issuer, subject);
        CertBuilder certBuilder = LambdasoftPKIProvider.getProvider().getCertBuilder();
        return new CertEntity(certBuilder.buildAcIssuerCert(caCertBuildEntity), privateKey, publicKey);
    }

    public static KeyPair genRSAKey() throws Exception {
        KeyPair keyPair = null;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", new BouncyCastleProvider());
        SecureRandom secureRandom = FixedSecureRandom.getInstance("SHA1PRNG", "SUN");
        keyPairGenerator.initialize(2048, secureRandom);
        keyPair = keyPairGenerator.generateKeyPair();
        if (keyPair == null) {
            throw new Exception("create Key is not success");
        }
        return keyPair;
    }
}
