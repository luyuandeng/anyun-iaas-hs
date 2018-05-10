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

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Date;

/**
 * Created by mr-tan on 16-3-9.
 */
public class CertBuilder {

    public X509Certificate buildAcIssuerCert(CACertBuildEntity caCertBuildEntity) throws Exception {
        X500Name issuer = new X500Name(caCertBuildEntity.getIssuer());
        BigInteger serial = caCertBuildEntity.getSerialNumber();
        Date NotBefore = caCertBuildEntity.getNotBeforeDate();
        Date NotAfter = caCertBuildEntity.getNotAfterDate();
        X500Name subject = new X500Name(caCertBuildEntity.getSubject());
        PublicKey publickey = caCertBuildEntity.getCaPublicKey();
        PrivateKey privateKey = caCertBuildEntity.getCaPrivateKey();
        X509v1CertificateBuilder x509v1CertificateBuilder = new JcaX509v1CertificateBuilder(issuer, serial, NotBefore, NotAfter, subject, publickey);
        X509CertificateHolder x509CertificateHolder = x509v1CertificateBuilder.build(new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(privateKey));
        return new JcaX509CertificateConverter().setProvider("BC").getCertificate(x509CertificateHolder);
    }

    public CertEntity buildCert(ClientCertBuildEntity clientCertBuildEntity) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey issPub = keyPair.getPublic();
        X500Name issuer = new X500Name(clientCertBuildEntity.getIssuer());
        BigInteger serial = BigInteger.valueOf(20);
        Date NotBefore = clientCertBuildEntity.getNotBeforeDate();
        Date NotAfter = clientCertBuildEntity.getNotAfterDate();
        X500Name subject = new X500Name(clientCertBuildEntity.getSubject());
        JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
        X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(issuer, serial, NotBefore, NotAfter, subject, clientCertBuildEntity.getPublicKey());
        x509v3CertificateBuilder.addExtension(Extension.subjectKeyIdentifier, false, extensionUtils.createSubjectKeyIdentifier(clientCertBuildEntity.getPublicKey()));
        x509v3CertificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, extensionUtils.createAuthorityKeyIdentifier(issPub));
        X509Certificate x509Certificate = new JcaX509CertificateConverter().
                setProvider("BC").getCertificate(x509v3CertificateBuilder.
                build(new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(clientCertBuildEntity.getPrivateKey())));
        CertEntity certEntity = new CertEntity(x509Certificate, clientCertBuildEntity.getPrivateKey(), clientCertBuildEntity.getPublicKey());
        return certEntity;

    }

}
