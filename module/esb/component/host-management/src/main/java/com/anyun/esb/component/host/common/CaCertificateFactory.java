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

package com.anyun.esb.component.host.common;

import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.PrivateKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 3/28/16
 */
public class CaCertificateFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(CaCertificateFactory.class);
    private X509Certificate caCertificate;
    private PrivateKey caPrivateKey;

    private static CaCertificateFactory factory;

    private CaCertificateFactory() {
    }

    public static CaCertificateFactory getFactory() {
        synchronized (CaCertificateFactory.class) {
            if(factory == null)
                factory = new CaCertificateFactory();
        }
        return factory;
    }

    public void initCertificate() throws Exception{
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        InputStream caInputStream = Resources.getResourceAsStream("cert/ca.crt");
        caCertificate = (X509Certificate) certificateFactory.generateCertificate(caInputStream);
        LOGGER.info("CA X.509 certificate read successfully");
        InputStream ocspIs = Resources.getResourceAsStream("cert/ca.pem");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ocspIs));
        PEMParser pemParser = new PEMParser(bufferedReader);
        PEMKeyPair keyPair = (PEMKeyPair) pemParser.readObject();
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        caPrivateKey = converter.getPrivateKey(keyPair.getPrivateKeyInfo());
        LOGGER.info("CA private key read successfully");
    }

    public X509Certificate getCaCertificate() {
        return caCertificate;
    }

    public PrivateKey getCaPrivateKey() {
        return caPrivateKey;
    }
}
