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

import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.Certificate;

/**
 * Created by mr-tan on 16-3-9.
 */
public class KeyStoreBuilder {

    static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

    public KeyStore buildKeyStore(String passwd) throws Exception{

        KeyStore keyStore = KeyStore.getInstance("JKS");

        keyStore.load(null,passwd==null?null:passwd.toCharArray());

        return keyStore;

    }

    public static KeyStore buildPKCS12Store(String passwd) throws Exception{

        KeyStore keyStore = KeyStore.getInstance("PKCS12","BC");

        keyStore.load(null,passwd==null?null:passwd.toCharArray());

        return keyStore;
    }

    public KeyStore buildStore(String passwd,String type,String type1) throws Exception{

        KeyStore keyStore = null;

        if(StringUtils.isNotEmpty(type1)){
            keyStore=KeyStore.getInstance(type.toUpperCase(),type1.toUpperCase());
        }else {
            keyStore = KeyStore.getInstance(type.toUpperCase());
        }

        keyStore.load(null,passwd==null?null:passwd.toCharArray());

        return keyStore;
    }

    public void addCert(KeyStore keyStore, String name,Certificate certificate, OutputStream outputStream,String passwd)
            throws Exception{
        if(keyStore.containsAlias(name)){
            throw new Exception("证书别名已存在");
        }
        keyStore.setCertificateEntry(name,certificate);

        keyStore.store(outputStream,passwd.toCharArray());

        outputStream.flush();

        outputStream.close();

    }

    public void addKey(KeyStore keyStore, String name, Key key,OutputStream outputStream,String passwd,Certificate[] chain) throws Exception{

        if(keyStore.containsAlias(name)){
            throw new Exception("证书别名已存在");
        }

        keyStore.setKeyEntry(name,key,passwd.toCharArray(),chain);

        keyStore.store(outputStream,passwd.toCharArray());

        outputStream.flush();

        outputStream.close();
    }
}
