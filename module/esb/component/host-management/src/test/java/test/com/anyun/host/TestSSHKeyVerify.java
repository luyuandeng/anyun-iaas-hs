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

package test.com.anyun.host;

import com.anyun.esb.component.host.common.SSHKeyVerifyHelper;
import org.junit.Test;

/**
 * @author twitchgg <twitchgg@yahoo.com>
 * @version 1.0
 * @date 6/15/16
 */
public class TestSSHKeyVerify extends BaseComponentTest {

    @Test
    public void testKey1() throws Exception {
        String key = "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDDEdtZGM+Ps37TKPtrhA0ZAcLoUsgfjRu6G7F8mFW12ed99E5SxtCE3" +
                "ktnnEdBvey/yJc2GM+ApcrHfOfiXV7CRrC2Dtw0eK/N1XOasI45N/4KgvsNYr7HuSB8aZ5mwvIUI0hqOZ1BcDNr34ahgpQBd9F" +
                "qutPMqwmm9KqXN4rw/XZKwrC8BJi86fUu6wERm+KtXMikcwZ3o6GfkPCOHRcVur/OcwwzrVe2bZ3N9A1XmVSX4lFLqMl0/FtLf" +
                "vjHgSeeKzLeS3HV5vf65kJyCL4ZB3Nq3DXrPZ2J1UqoQRHUbfoMBikN2CFUVqXyD+iTjE8nP6a1loV4KCtBsL93CLIJ twitchgg@Office.local";
        SSHKeyVerifyHelper.verify(key);
    }
}
