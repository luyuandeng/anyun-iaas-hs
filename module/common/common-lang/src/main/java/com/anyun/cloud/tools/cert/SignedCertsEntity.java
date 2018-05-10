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

import java.security.Key;
import java.security.cert.Certificate;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/20/16
 */
public class SignedCertsEntity {

    private Certificate[] signedCerts;

    private Key key;

    private char[] passwd;

    public SignedCertsEntity(Certificate[] signedCerts,Key key,char[] passwd){

        this.signedCerts = signedCerts;

        this.key = key;

        this.passwd = passwd;
    }

    public Certificate[] getSignedCerts() {
        return signedCerts;
    }

    public char[] getPasswd() {
        return passwd;
    }

    public Key getKey() {
        return key;
    }
}