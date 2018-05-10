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
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequest;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.Security;

/**
 * Created by mr-tan on 16-3-9.
 */
public class PKCS10Ustils {

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public static JcaPKCS10CertificationRequest generateCertRequest(
            KeyPair pair, String encryptType,
            String X500Principal)throws Exception{

        JcaPKCS10CertificationRequestBuilder requestBuilder = new JcaPKCS10CertificationRequestBuilder(new X500Name(X500Principal),pair.getPublic());

        PKCS10CertificationRequest pkcsrequest = requestBuilder.build(new JcaContentSignerBuilder(encryptType).setProvider("BC").build(pair.getPrivate()));

        JcaPKCS10CertificationRequest request =new JcaPKCS10CertificationRequest(pkcsrequest.getEncoded()).setProvider("BC");

        return request;
    }

    public static PKCS10CertificationRequest readCSR(byte[] bytes)throws Exception{

        if(bytes==null || bytes.length==0){

                return null;
        }

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        PEMParser pemParser = new PEMParser(new InputStreamReader(byteArrayInputStream));

        PKCS10CertificationRequest request = (PKCS10CertificationRequest) pemParser.readObject();

        pemParser.close();

        byteArrayInputStream.close();

        return request;
    }

}
