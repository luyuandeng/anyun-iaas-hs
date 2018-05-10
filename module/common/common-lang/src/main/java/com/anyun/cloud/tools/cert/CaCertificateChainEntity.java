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

import java.security.cert.X509Certificate;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/16/16
 */
public class CaCertificateChainEntity {
    private X509Certificate[] chain;
    private CertEntity entity;

    public X509Certificate[] getChain() {
        return chain;
    }

    public void setChain(X509Certificate[] chain) {
        this.chain = chain;
    }

    public CertEntity getEntity() {
        return entity;
    }

    public void setEntity(CertEntity entity) {
        this.entity = entity;
    }
}
