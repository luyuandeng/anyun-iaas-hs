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
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;

/**
 * Created by mr-tan on 16-3-9.
 */
public class KeyBuilder {

    public KeyFactory keyFactory;

    public KeyBuilder(KeyFactory keyFactory){

        this.keyFactory = keyFactory;
    }

    public PrivateKey buildPrivateKey(RSAPrivateCrtKeySpec keySpec) throws Exception{

        return keyFactory.generatePrivate(keySpec);
    }

    public PublicKey buildPublicKey(RSAPublicKeySpec keySpec) throws Exception{

        return keyFactory.generatePublic(keySpec);
    }


    public RSAPublicKeySpec buildRSAPublicKeySpec(BigInteger modulus,BigInteger publicExponent) throws Exception{

        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus,publicExponent);

        return rsaPublicKeySpec;
    }

    public RSAPrivateCrtKeySpec buildRSAPrivateCrtKeySpec(
            BigInteger modulus,BigInteger publicExponent,
            BigInteger privateExponent,BigInteger primeP,
            BigInteger primeQ,BigInteger primeExponentP,
            BigInteger primeExponentQ,BigInteger crtCoefficient)throws Exception{

        RSAPrivateCrtKeySpec rsaPrivateCrtKeySpec = new RSAPrivateCrtKeySpec(
                modulus,publicExponent,privateExponent,primeP,primeQ,primeExponentP,primeExponentQ,crtCoefficient);

        return rsaPrivateCrtKeySpec;
    }
}
