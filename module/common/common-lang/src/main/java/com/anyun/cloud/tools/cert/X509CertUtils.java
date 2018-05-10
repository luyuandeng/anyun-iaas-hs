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

import com.anyun.cloud.tools.StringUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Date;

/**
 * Created by mr-tan on 16-3-9.
 */
public class X509CertUtils {
    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static X509Certificate generateV3Certificate(
            KeyPair keyPair, String issuerDN, String subjectDN, Date notBefore,
            Date notAfter) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        X500Name issuer = new X500Name(issuerDN);
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        X500Name subject = new X500Name(subjectDN);
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair pair = keyPairGenerator.generateKeyPair();
        PublicKey isspub = pair.getPublic();
        JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
        X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(issuer, serial, notBefore, notAfter, subject, keyPair.getPublic());
        x509v3CertificateBuilder.addExtension(Extension.subjectKeyIdentifier, false, extensionUtils.createSubjectKeyIdentifier(keyPair.getPublic()));
        x509v3CertificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, extensionUtils.createAuthorityKeyIdentifier(isspub));
        X509Certificate x509Certificate = new JcaX509CertificateConverter().
                setProvider("BC").getCertificate(x509v3CertificateBuilder.
                build(new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(keyPair.getPrivate())));
        return x509Certificate;
    }

    public static X509Certificate generateV3SelfSignedCertificate(
            KeyPair keyPair, String issuerDN, String subjectDN, Date notBefore,
            Date notAfter) throws Exception {
        X509Certificate x509Certificate = generateV3Certificate(
                keyPair, issuerDN, subjectDN, notBefore, notAfter);
        x509Certificate.verify(x509Certificate.getPublicKey());
        x509Certificate.checkValidity(new Date());
        return x509Certificate;
    }

    public static X509Certificate generateV3SelfSignedCertificate(
            PrivateKey privateKey, JcaPKCS10CertificationRequest request,
            Long notAfter) throws Exception {
        if (notAfter == null) {
            notAfter = 365 * 24 * 60 * 60 * 1000L;
        }
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        BigInteger serial = BigInteger.valueOf(System.nanoTime());
        X500Name issuer = new X500Name(request.getSubject().toString());
        Date NotBefore = new Date();
        Date NotAfter = new Date(System.currentTimeMillis() + notAfter);
        X500Name subject = new X500Name(request.getSubject().toString());
        PublicKey publicKey = request.getPublicKey();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PublicKey isspub = keyPair.getPublic();
        JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
        X509v3CertificateBuilder x509v3CertificateBuilder = new JcaX509v3CertificateBuilder(issuer, serial, NotBefore, NotAfter, subject, publicKey);
        x509v3CertificateBuilder.addExtension(Extension.subjectKeyIdentifier, false, extensionUtils.createSubjectKeyIdentifier(publicKey));
        x509v3CertificateBuilder.addExtension(Extension.authorityKeyIdentifier, false, extensionUtils.createAuthorityKeyIdentifier(isspub));
        X509Certificate x509Certificate = new JcaX509CertificateConverter().
                setProvider("BC").getCertificate(x509v3CertificateBuilder.
                build(new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(privateKey)));
        return x509Certificate;

    }

    public static X509Certificate generateV1SelfSignedCertificate(KeyPair keyPair, String issuerDN,
                                                                  String subjectDN, Date notBefore,
                                                                  Date notAfter) throws Exception {
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        X500Name issuer = new X500Name(issuerDN);
        X500Name subject = new X500Name(subjectDN);
        Date NotBefore = notBefore;
        Date NotAfter = notAfter;
        X509v1CertificateBuilder x509v1CertificateBuilder =
                new JcaX509v1CertificateBuilder(issuer, serial, NotBefore, NotAfter, subject, keyPair.getPublic());
        X509CertificateHolder cert =
                x509v1CertificateBuilder.build(new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(keyPair.getPrivate()));
        X509Certificate x509Certificate = new JcaX509CertificateConverter().setProvider("BC").getCertificate(cert);
        return x509Certificate;
    }


    public static X509Certificate generateClientCert(PublicKey publicKey, PrivateKey caprivateKey,
                                                     PublicKey caPublicKey, String subjectDN,
                                                     X509Certificate cacert) throws Exception {
        X500Name issuer = new X500Name(cacert.getIssuerDN().toString());
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date NotBefore = new Date(System.currentTimeMillis());
        Date NotAfter = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 12);
        X500Name subject = new X500Name(subjectDN);
        X509v3CertificateBuilder x509v3CertificateBuilder =
                new JcaX509v3CertificateBuilder(issuer, serial, NotBefore, NotAfter, subject, publicKey);
        X509Certificate x509Certificate = new JcaX509CertificateConverter().
                setProvider("BC").getCertificate(x509v3CertificateBuilder.
                build(new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(caprivateKey)));
        x509Certificate.verify(caPublicKey);
        x509Certificate.checkValidity(new Date());
        return x509Certificate;
    }

    public static CaCertificateChainEntity buildCaCertificateChain(int expirationDate,String encryptType, String x500Principal) throws Exception {
        if(StringUtils.isEmpty(encryptType))
            encryptType = "SHA256withRSA";
        LambdasoftPKIProvider provider = LambdasoftPKIProvider.getProvider();
        CaCertBuilder caCertBuilder = provider.getCaCertBuilder();
        CertEntity entity = caCertBuilder.buildLambdasoftDefaultCA(x500Principal);
        KeyPair keyPair = new KeyPair(entity.getPublicKey(), entity.getPrivateKey());
        JcaPKCS10CertificationRequest request = PKCS10Ustils.generateCertRequest(keyPair, encryptType, x500Principal);

        String issuerDN = request.getSubject().toString();
        Date notBefore = new Date(System.currentTimeMillis());
        Date notAfter = new Date(System.currentTimeMillis() + expirationDate * 24 * 60 * 60 * 1000L);
        X509Certificate rootCert = X509CertUtils.generateV1SelfSignedCertificate(
                keyPair, issuerDN, issuerDN, notBefore, notAfter);
        X500Name issuer = new X500Name(rootCert.getIssuerDN().toString());
        BigInteger serial = BigInteger.valueOf(System.currentTimeMillis());
        Date NotBefore = new Date(System.currentTimeMillis());
        Date NotAfter = new Date(System.currentTimeMillis() + expirationDate * 24 * 60 * 60 * 1000L);
        X500Name subject = request.getSubject();
        PublicKey publicKey = request.getPublicKey();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair pair = keyPairGenerator.generateKeyPair();
        PublicKey isspub = pair.getPublic();
        JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();
        X509v3CertificateBuilder x509v3CertificateBuilder =
                new JcaX509v3CertificateBuilder(issuer, serial, NotBefore, NotAfter, subject, publicKey);
        x509v3CertificateBuilder.addExtension(
                Extension.subjectKeyIdentifier, false, extensionUtils.createSubjectKeyIdentifier(publicKey));
        x509v3CertificateBuilder.addExtension(Extension.authorityKeyIdentifier, false,
                extensionUtils.createAuthorityKeyIdentifier(isspub));
        X509Certificate x509Certificate = new JcaX509CertificateConverter().
                setProvider("BC").getCertificate(x509v3CertificateBuilder.
                build(new JcaContentSignerBuilder("SHA256withRSA").setProvider("BC").build(keyPair.getPrivate())));
        x509Certificate.verify(publicKey);
        x509Certificate.checkValidity(new Date());

        X509Certificate[] chain = new X509Certificate[]{x509Certificate, rootCert};
        CaCertificateChainEntity chainEntity = new CaCertificateChainEntity();
        chainEntity.setChain(chain);
        chainEntity.setEntity(entity);
        return chainEntity;
    }

    public static String generateJcaPEMObjectToBase64(Object pem) throws Exception{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        JcaPEMWriter pemWriter = new JcaPEMWriter(writer);
        pemWriter.writeObject(pem);
        pemWriter.flush();
        pemWriter.close();
        String base64 = Base64.getEncoder().encodeToString(outputStream.toByteArray());
        return base64;
    }

}
